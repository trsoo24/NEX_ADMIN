package com.example.admin.common.service.gdcb.util;


import org.apache.commons.io.IOUtils;
import org.bouncycastle.bcpg.*;
import org.bouncycastle.bcpg.sig.KeyFlags;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.openpgp.*;
import org.bouncycastle.openpgp.operator.PBESecretKeyDecryptor;
import org.bouncycastle.openpgp.operator.PGPContentSignerBuilder;
import org.bouncycastle.openpgp.operator.bc.*;

import java.io.*;
import java.security.NoSuchProviderException;
import java.security.Provider;
import java.security.SecureRandom;
import java.security.Security;
import java.util.Date;
import java.util.Iterator;

/**
 * PGP 유틸리티
 * 
 * @author other
 *
 */
public class PGPUtils {

    private static final int   BUFFER_SIZE = 1 << 16; // should always be power of 2
    private static final int   KEY_FLAGS = 27;
    private static final int[] MASTER_KEY_CERTIFICATION_TYPES = new int[]{
    	PGPSignature.POSITIVE_CERTIFICATION,
    	PGPSignature.CASUAL_CERTIFICATION,
    	PGPSignature.NO_CERTIFICATION,
    	PGPSignature.DEFAULT_CERTIFICATION
    };

	@SuppressWarnings("unchecked")
	public static PGPPublicKey readPublicKey(InputStream in) throws IOException, PGPException {

        PGPPublicKeyRingCollection keyRingCollection = new PGPPublicKeyRingCollection(PGPUtil.getDecoderStream(in));

        //
        // we just loop through the collection till we find a key suitable for encryption, in the real
        // world you would probably want to be a bit smarter about this.
        //
        PGPPublicKey publicKey = null;

        //
        // iterate through the key rings.
        //
        Iterator<PGPPublicKeyRing> rIt = keyRingCollection.getKeyRings();

        while (publicKey == null && rIt.hasNext()) {
            PGPPublicKeyRing kRing = rIt.next();
            Iterator<PGPPublicKey> kIt = kRing.getPublicKeys();
            while (publicKey == null && kIt.hasNext()) {
                PGPPublicKey key = kIt.next();
                if (key.isEncryptionKey()) {
                    publicKey = key;
                }
            }
        }

        if (publicKey == null) {
            throw new IllegalArgumentException("Can't find public key in the key ring.");
        }
        if (!isForEncryption(publicKey)) {
            throw new IllegalArgumentException("KeyID " + publicKey.getKeyID() + " not flagged for encryption.");
        }

        return publicKey;
    }

	@SuppressWarnings("unchecked")
	public static PGPSecretKey readSecretKey(InputStream in) throws IOException, PGPException {

        PGPSecretKeyRingCollection keyRingCollection = new PGPSecretKeyRingCollection(PGPUtil.getDecoderStream(in));

        //
        // We just loop through the collection till we find a key suitable for signing.
        // In the real world you would probably want to be a bit smarter about this.
        //
        PGPSecretKey secretKey = null;

        Iterator<PGPSecretKeyRing> rIt = keyRingCollection.getKeyRings();
        while (secretKey == null && rIt.hasNext()) {
            PGPSecretKeyRing keyRing = rIt.next();
            Iterator<PGPSecretKey> kIt = keyRing.getSecretKeys();
            while (secretKey == null && kIt.hasNext()) {
                PGPSecretKey key = kIt.next();
                if (key.isSigningKey()) {
                    secretKey = key;
                }
            }
        }

        // Validate secret key
        if (secretKey == null) {
            throw new IllegalArgumentException("Can't find private key in the key ring.");
        }
        if (!secretKey.isSigningKey()) {
            throw new IllegalArgumentException("Private key does not allow signing.");
        }
        if (secretKey.getPublicKey().isRevoked()) {
            throw new IllegalArgumentException("Private key has been revoked.");
        }
        if (!hasKeyFlags(secretKey.getPublicKey(), KeyFlags.SIGN_DATA)) {
            throw new IllegalArgumentException("Key cannot be used for signing.");
        }

        return secretKey;
    }

    /**
     * Load a secret key ring collection from keyIn and find the private key corresponding to
     * keyID if it exists.
     *
     * @param keyID keyID we want.
     * @param pass passphrase to decrypt secret key with.
     * @return
     * @throws IOException
     * @throws PGPException
     * @throws NoSuchProviderException
     */
	public static PGPPrivateKey findPrivateKey(PGPSecretKeyRingCollection pgpSec, long keyID, char[] pass) throws IOException, PGPException, NoSuchProviderException {
		//        PGPSecretKeyRingCollection pgpSec = new PGPSecretKeyRingCollection(PGPUtil.getDecoderStream(keyIn));
		return findPrivateKey(pgpSec.getSecretKey(keyID), pass);

	}

    /**
     * Load a secret key and find the private key in it
     * @param pgpSecKey The secret key
     * @param pass passphrase to decrypt secret key with
     * @return
     * @throws PGPException
     */
    public static PGPPrivateKey findPrivateKey(PGPSecretKey pgpSecKey, char[] pass) 	throws PGPException {
    	if (pgpSecKey == null) {
    		return null;
    	}

        PBESecretKeyDecryptor decryptor = new BcPBESecretKeyDecryptorBuilder(new BcPGPDigestCalculatorProvider()).build(pass);
        return pgpSecKey.extractPrivateKey(decryptor);
    }

    /**
     * decrypt and Verify the passed in message stream
     */
    @SuppressWarnings("unchecked")
    public static void decryptAndVerify(InputStream in, OutputStream out, InputStream keyIn, PGPPublicKey validationKey, char[] passwd) throws Exception {
    	
    	InputStream clear = null;
    	InputStream unc = null;
    	
    	try {
    		Provider provider = new BouncyCastleProvider();
    		Security.addProvider(provider);
    		
    		in = PGPUtil.getDecoderStream(in);
    		
    		PGPObjectFactory pgpF = new PGPObjectFactory(in);
    		PGPEncryptedDataList enc;
    		
    		Object o = pgpF.nextObject();
    		
    		// the first object might be a PGP marker packet.
    		if (o instanceof  PGPEncryptedDataList) {
    			enc = (PGPEncryptedDataList) o;
    		} else {
    			enc = (PGPEncryptedDataList) pgpF.nextObject();
    		}
    		
    		// find the secret key
    		Iterator<PGPPublicKeyEncryptedData> it = enc.getEncryptedDataObjects();
    		PGPPrivateKey sKey = null;
    		PGPPublicKeyEncryptedData pbe = null;
    		PGPSecretKeyRingCollection pgpSec = new PGPSecretKeyRingCollection(PGPUtil.getDecoderStream(keyIn));
    		
    		while (sKey == null && it.hasNext()) {
    			pbe = it.next();
    			
    			sKey = findPrivateKey(pgpSec, pbe.getKeyID(), passwd);
    		}
    		
    		if (sKey == null) {
    			throw new IllegalArgumentException("Secret key for message not found." + pbe.getKeyID());
    		}
    		
    		clear = pbe.getDataStream(new BcPublicKeyDataDecryptorFactory(sKey));
    		
    		PGPObjectFactory plainFact = new PGPObjectFactory(clear);
    		
    		Object message = plainFact.nextObject();
    		
    		PGPLiteralData ld = null;
    		
    		if (message instanceof PGPCompressedData cData) {
                PGPObjectFactory pgpFact = new PGPObjectFactory(cData.getDataStream());
    			
    			message = pgpFact.nextObject();
    		}
    		
    		if (message instanceof  PGPLiteralData) {
    			ld = (PGPLiteralData) message;
    			
    			unc = ld.getInputStream();
    			int ch;
    			
    			while ((ch = unc.read()) >= 0) {
    				out.write(ch);
    			}
    		
    		} else if (message instanceof  PGPOnePassSignatureList) {
    			try {
    				PGPOnePassSignatureList list = (PGPOnePassSignatureList) message;
    				PGPOnePassSignature ops = list.get(0);
    				//서명 확인
    				ops.init(new BcPGPContentVerifierBuilderProvider(), validationKey);
    				
    				PGPLiteralData literalData = (PGPLiteralData)plainFact.nextObject();
    				
    				unc = literalData.getInputStream();
    				
    				int    ch;
    				
    				while ((ch = unc.read()) >= 0) {
    					out.write(ch);
    				}
    				
    			} catch (IOException ioe) {
    				throw new PGPException("unknown PGP public key algorithm encountered");
    			} catch (Exception e) {
    				throw new PGPException("Encrypted message contains a signed message - not literal data.");
    			}
    		
    		} else {
    			throw new PGPException("Message is not a simple encrypted file - type unknown.");
    		}
    		
    		if (pbe.isIntegrityProtected()) {
    			if (!pbe.verify()) {
    				throw new PGPException("Message failed integrity check");
    			}
			}
		} finally {
			if (clear != null) {
				clear.close();
			}
			if (unc != null) {
				unc.close();
			}
		}
	}

	public static void encryptFile(OutputStream out, String fileName, PGPPublicKey encKey, boolean armor, boolean withIntegrityCheck) throws IOException, NoSuchProviderException, PGPException {
		OutputStream cOut = null;

		try {
			Security.addProvider(new BouncyCastleProvider());

			if (armor) {
				out = new ArmoredOutputStream(out);
			}

			ByteArrayOutputStream bOut = new ByteArrayOutputStream();
			PGPCompressedDataGenerator comData = new PGPCompressedDataGenerator(CompressionAlgorithmTags.ZIP);

			PGPUtil.writeFileToLiteralData(comData.open(bOut), PGPLiteralData.BINARY, new File(fileName));

			comData.close();

			BcPGPDataEncryptorBuilder dataEncryptor = new BcPGPDataEncryptorBuilder(SymmetricKeyAlgorithmTags.TRIPLE_DES);
			dataEncryptor.setWithIntegrityPacket(withIntegrityCheck);
			dataEncryptor.setSecureRandom(new SecureRandom());

			PGPEncryptedDataGenerator encryptedDataGenerator = new PGPEncryptedDataGenerator(dataEncryptor);
			encryptedDataGenerator.addMethod(new BcPublicKeyKeyEncryptionMethodGenerator(encKey));

			byte[] bytes = bOut.toByteArray();
			cOut = encryptedDataGenerator.open(out, bytes.length);
			cOut.write(bytes);

		} finally {
			if (cOut != null) {
				cOut.close();
			}
		}
	}

    @SuppressWarnings("unchecked")
	public static void signEncryptFile(
								        OutputStream out,
								        String fileName,
								        PGPPublicKey publicKey,
								        PGPSecretKey secretKey,
								        String password,
								        boolean armor,
								        boolean withIntegrityCheck ) throws Exception {
    	OutputStream encryptedOut = null;
    	OutputStream compressedOut = null;
    	OutputStream literalOut = null;
    	FileInputStream in = null;
    	PGPCompressedDataGenerator compressedDataGenerator = null;
    	
    	try {
    		// Initialize Bouncy Castle security provider
    		Provider provider = new BouncyCastleProvider();
    		Security.addProvider(provider);
    		
    		if (armor) {
    			out = new ArmoredOutputStream(out);
    		}
    		
    		BcPGPDataEncryptorBuilder dataEncryptor = new BcPGPDataEncryptorBuilder(SymmetricKeyAlgorithmTags.TRIPLE_DES);
    		dataEncryptor.setWithIntegrityPacket(withIntegrityCheck);
    		dataEncryptor.setSecureRandom(new SecureRandom());
    		
    		PGPEncryptedDataGenerator encryptedDataGenerator = new PGPEncryptedDataGenerator(dataEncryptor);
    		encryptedDataGenerator.addMethod(new BcPublicKeyKeyEncryptionMethodGenerator(publicKey));
    		
    		encryptedOut = encryptedDataGenerator.open(out, new byte[PGPUtils.BUFFER_SIZE]);
    		
    		// Initialize compressed data generator
    		compressedDataGenerator = new PGPCompressedDataGenerator(CompressionAlgorithmTags.ZIP);
    		compressedOut = compressedDataGenerator.open(encryptedOut, new byte [PGPUtils.BUFFER_SIZE]);
    		
    		// Initialize signature generator
    		PGPPrivateKey privateKey = findPrivateKey(secretKey, password.toCharArray());
    		
    		PGPContentSignerBuilder signerBuilder = new BcPGPContentSignerBuilder(secretKey.getPublicKey().getAlgorithm(), HashAlgorithmTags.SHA1);
    		
    		PGPSignatureGenerator signatureGenerator = new PGPSignatureGenerator(signerBuilder);
    		signatureGenerator.init(PGPSignature.BINARY_DOCUMENT, privateKey);
    		
    		boolean firstTime = true;
    		Iterator<String> it = secretKey.getPublicKey().getUserIDs();
    		while (it.hasNext() && firstTime) {
    			PGPSignatureSubpacketGenerator spGen = new PGPSignatureSubpacketGenerator();
    			spGen.setSignerUserID(false, it.next());
    			signatureGenerator.setHashedSubpackets(spGen.generate());
    			// Exit the loop after the first iteration
    			firstTime = false;
    		}
    		signatureGenerator.generateOnePassVersion(false).encode(compressedOut);
    		
    		// Initialize literal data generator
    		PGPLiteralDataGenerator literalDataGenerator = new PGPLiteralDataGenerator();
    		literalOut = literalDataGenerator.open(
    				compressedOut,
    				PGPLiteralData.BINARY,
    				fileName,
    				new Date(),
    				new byte [PGPUtils.BUFFER_SIZE] );
    		
    		// Main loop - read the "in" stream, compress, encrypt and write to the "out" stream
    		in = new FileInputStream(fileName);
    		byte[] buf = new byte[PGPUtils.BUFFER_SIZE];
    		int len;
    		
    		while ((len = in.read(buf)) > 0) {
    			literalOut.write(buf, 0, len);
    			signatureGenerator.update(buf, 0, len);
    		}
    		
    		literalDataGenerator.close();
    		// Generate the signature, compress, encrypt and write to the "out" stream
    		signatureGenerator.generate().encode(compressedOut);
    		compressedDataGenerator.close();
    		encryptedDataGenerator.close();
    		if (armor) {
    			out.close();
    		}
		} finally {
			if (encryptedOut != null) {
				encryptedOut.close();
			}
			if (compressedOut != null) {
				compressedOut.close();
			}
			if (literalOut != null) {
				literalOut.close();
			}
			if (in != null) {
				in.close();
			}
			if (compressedDataGenerator != null) {
				compressedDataGenerator.close();
			}
		}
	}

	public static boolean verifyFile(InputStream in, InputStream keyIn, String extractContentFile) throws Exception {
		PGPOnePassSignature ops = null;
		PGPSignatureList p3 = null;
		InputStream dIn = null;
		FileOutputStream out = null;

		try {

			in = PGPUtil.getDecoderStream(in);

			PGPObjectFactory pgpFact = new PGPObjectFactory(in);
			PGPCompressedData c1 = (PGPCompressedData)pgpFact.nextObject();

			pgpFact = new PGPObjectFactory(c1.getDataStream());

			PGPOnePassSignatureList p1 = (PGPOnePassSignatureList)pgpFact.nextObject();

			ops = p1.get(0);

			PGPLiteralData p2 = (PGPLiteralData)pgpFact.nextObject();

			dIn = p2.getInputStream();

			IOUtils.copy(dIn, new FileOutputStream(extractContentFile));

			int ch;
			PGPPublicKeyRingCollection pgpRing = new PGPPublicKeyRingCollection(PGPUtil.getDecoderStream(keyIn));

			PGPPublicKey key = pgpRing.getPublicKey(ops.getKeyID());

			out = new FileOutputStream(p2.getFileName());

			ops.init(new BcPGPContentVerifierBuilderProvider(), key);

			while ((ch = dIn.read()) >= 0) {
				ops.update((byte)ch);
				out.write(ch);
			}

			out.close();

			p3 = (PGPSignatureList)pgpFact.nextObject();

		} finally {
			if (dIn != null) {
				dIn.close();
			}
			if (out != null) {
				out.close();
			}
		}

		return ops.verify(p3.get(0));
	}

    /**
     * From LockBox Lobs PGP Encryption tools.
     * http://www.lockboxlabs.org/content/downloads
     *
     * I didn't think it was worth having to import a 4meg lib for three methods
     * @param key
     * @return
     */
	public static boolean isForEncryption(PGPPublicKey key) {
		if (key.getAlgorithm() == PublicKeyAlgorithmTags.RSA_SIGN || key.getAlgorithm() == PublicKeyAlgorithmTags.DSA || key.getAlgorithm() == PublicKeyAlgorithmTags.EC || key.getAlgorithm() == PublicKeyAlgorithmTags.ECDSA) {
			return false;
		}

		return hasKeyFlags(key, KeyFlags.ENCRYPT_COMMS | KeyFlags.ENCRYPT_STORAGE);
	}

    /**
     * From LockBox Lobs PGP Encryption tools.
     * http://www.lockboxlabs.org/content/downloads
     *
     * I didn't think it was worth having to import a 4meg lib for three methods
     * @param
     * @return
     */
	@SuppressWarnings("unchecked")
	private static boolean hasKeyFlags(PGPPublicKey encKey, int keyUsage) {
		if (encKey.isMasterKey()) {
			for (int i = 0; i != PGPUtils.MASTER_KEY_CERTIFICATION_TYPES.length; i++) {
				for (Iterator<PGPSignature> eIt = encKey.getSignaturesOfType(PGPUtils.MASTER_KEY_CERTIFICATION_TYPES[i]); eIt.hasNext();) {
					PGPSignature sig = eIt.next();
					if (!isMatchingUsage(sig, keyUsage)) {
						return false;
					}
				}
			}
		} else {
			for (Iterator<PGPSignature> eIt = encKey.getSignaturesOfType(PGPSignature.SUBKEY_BINDING); eIt.hasNext();) {
				PGPSignature sig = eIt.next();
				if (!isMatchingUsage(sig, keyUsage)) {
					return false;
				}
			}
		}
		
		return true;
	}

    /**
     * From LockBox Lobs PGP Encryption tools.
     * http://www.lockboxlabs.org/content/downloads
     *
     * I didn't think it was worth having to import a 4meg lib for three methods
     * @param
     * @return
     */
    private static boolean isMatchingUsage(PGPSignature sig, int keyUsage) {
        if (sig.hasSubpackets()) {
            PGPSignatureSubpacketVector sv = sig.getHashedSubPackets();
            if (sv.hasSubpacket(PGPUtils.KEY_FLAGS)) {
                return sv.getKeyFlags() != 0 || keyUsage != 0;
            }
        }
        
        return true;
    }
}
