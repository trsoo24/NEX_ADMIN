const l=t=>`${t.slice(0,3)}-${t.slice(3,7)}-${t.slice(7)}`,i=t=>{if(t!=null)return t.replace(/(^02.{0}|^01.{1}|[0-9]{3})([0-9]+)([0-9]{4})/,"$1-****-$3")},c=t=>{if(t==null)return;const s=t.slice(0,3),n="*".repeat(Math.max(0,t.length-3));return`${s}${n}`},o=t=>{if(t==null)return;const[s,n]=t.split("@"),e=s.slice(0,3),r="*".repeat(Math.max(0,s.length-3));return`${e}${r}@${n}`},u=t=>{if(t==null)return;const s=t.length,n=t.substring(0,1),e=t.substring(t.length-1,t.length);let r="";for(var a=0;a<s-2;a++)r+="*";return`${n}${r}${e}`};export{o as a,u as b,c,l as f,i as m};