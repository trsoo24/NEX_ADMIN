import{m as H,c as J,u as K,b as _,f as L,d as Q,e as W}from"./VField-Cziex-qH.js";import{f as X}from"./VGrid-DeylvMMY.js";import{p as Y,q as Z,a3 as ee,$ as v,e as x,B as te,a5 as ne,d as n,G as V,D as le,ae,F as g,bq as ue,af as S,ag as ie}from"./index-BNrJ-Dvj.js";import{I as oe}from"./index-BPvLzGhW.js";const re=["color","file","time","date","datetime-local","week","month"],se=Y({autofocus:Boolean,counter:[Boolean,Number,String],counterValue:[Number,Function],prefix:String,placeholder:String,persistentPlaceholder:Boolean,persistentCounter:Boolean,suffix:String,role:String,type:{type:String,default:"text"},modelModifiers:Object,...H(),...J()},"VTextField"),xe=Z()({name:"VTextField",directives:{Intersect:oe},inheritAttrs:!1,props:se(),emits:{"click:control":e=>!0,"mousedown:control":e=>!0,"update:focused":e=>!0,"update:modelValue":e=>!0},setup(e,w){let{attrs:m,emit:y,slots:l}=w;const i=ee(e,"modelValue"),{isFocused:r,focus:D,blur:A}=K(e),B=v(()=>typeof e.counterValue=="function"?e.counterValue(i.value):typeof e.counterValue=="number"?e.counterValue:(i.value??"").toString().length),R=v(()=>{if(m.maxlength)return m.maxlength;if(!(!e.counter||typeof e.counter!="number"&&typeof e.counter!="string"))return e.counter}),C=v(()=>["plain","underlined"].includes(e.variant));function T(t,a){var u,o;!e.autofocus||!t||(o=(u=a[0].target)==null?void 0:u.focus)==null||o.call(u)}const F=x(),h=x(),s=x(),p=v(()=>re.includes(e.type)||e.persistentPlaceholder||r.value||e.active);function d(){var t;s.value!==document.activeElement&&((t=s.value)==null||t.focus()),r.value||D()}function M(t){y("mousedown:control",t),t.target!==s.value&&(d(),t.preventDefault())}function N(t){d(),y("click:control",t)}function E(t){t.stopPropagation(),d(),S(()=>{i.value=null,ie(e["onClick:clear"],t)})}function q(t){var u;const a=t.target;if(i.value=a.value,(u=e.modelModifiers)!=null&&u.trim&&["text","search","password","tel","url"].includes(e.type)){const o=[a.selectionStart,a.selectionEnd];S(()=>{a.selectionStart=o[0],a.selectionEnd=o[1]})}}return te(()=>{const t=!!(l.counter||e.counter!==!1&&e.counter!=null),a=!!(t||l.details),[u,o]=ne(m),{modelValue:ce,...O}=_.filterProps(e),U=L(e);return n(_,V({ref:F,modelValue:i.value,"onUpdate:modelValue":c=>i.value=c,class:["v-text-field",{"v-text-field--prefixed":e.prefix,"v-text-field--suffixed":e.suffix,"v-input--plain-underlined":C.value},e.class],style:e.style},u,O,{centerAffix:!C.value,focused:r.value}),{...l,default:c=>{let{id:f,isDisabled:k,isDirty:P,isReadonly:j,isValid:z}=c;return n(Q,V({ref:h,onMousedown:M,onClick:N,"onClick:clear":E,"onClick:prependInner":e["onClick:prependInner"],"onClick:appendInner":e["onClick:appendInner"],role:e.role},U,{id:f.value,active:p.value||P.value,dirty:P.value||e.dirty,disabled:k.value,focused:r.value,error:z.value===!1}),{...l,default:G=>{let{props:{class:b,...$}}=G;const I=le(n("input",V({ref:s,value:i.value,onInput:q,autofocus:e.autofocus,readonly:j.value,disabled:k.value,name:e.name,placeholder:e.placeholder,size:1,type:e.type,onFocus:d,onBlur:A},$,o),null),[[ae("intersect"),{handler:T},null,{once:!0}]]);return n(g,null,[e.prefix&&n("span",{class:"v-text-field__prefix"},[n("span",{class:"v-text-field__prefix__text"},[e.prefix])]),l.default?n("div",{class:b,"data-no-activator":""},[l.default(),I]):ue(I,{class:b}),e.suffix&&n("span",{class:"v-text-field__suffix"},[n("span",{class:"v-text-field__suffix__text"},[e.suffix])])])}})},details:a?c=>{var f;return n(g,null,[(f=l.details)==null?void 0:f.call(l,c),t&&n(g,null,[n("span",null,null),n(W,{active:e.persistentCounter||r.value,value:B.value,max:R.value,disabled:e.disabled},l.counter)])])}:void 0})}),X({},F,h,s)}});export{xe as V,se as m};