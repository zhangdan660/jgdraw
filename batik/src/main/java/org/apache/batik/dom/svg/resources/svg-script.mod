<!ENTITY % SVG.script.qname "script" >

<!-- Attribute Collections (Default) ................... -->

<!ENTITY % SVG.Core.attrib "" >
<!ENTITY % SVG.XLink.attrib "" >
<!ENTITY % SVG.External.attrib "" >

<!-- SVG.Script.class .................................. -->

<!ENTITY % SVG.Script.extra.class "" >

<!ENTITY % SVG.Script.class
    "| %SVG.script.qname; %SVG.Script.extra.class;"
>

<!-- script: Script Element ............................ -->

<!ENTITY % SVG.script.extra.content "" >

<!ENTITY % SVG.script.element "INCLUDE" >
<![%SVG.script.element;[
<!ENTITY % SVG.script.content
    "( #PCDATA %SVG.script.extra.content; )*"
>
<!ELEMENT %SVG.script.qname; %SVG.script.content; >
<!-- end of SVG.script.element -->]]>

<!ENTITY % SVG.script.attlist "INCLUDE" >
<![%SVG.script.attlist;[
<!ATTLIST %SVG.script.qname;
    %SVG.Core.attrib;
    %SVG.XLink.attrib;
    %SVG.External.attrib;
    type %ContentType.datatype; #REQUIRED
>
<!-- end of SVG.script.attlist -->]]>

<!-- end of svg-script.mod -->
