<!ENTITY % ClassList.datatype "CDATA" >

<!-- comma-separated list of media descriptors. -->
<!ENTITY % MediaDesc.datatype "CDATA" >

<!-- style sheet data -->
<!ENTITY % StyleSheet.datatype "CDATA" >

<!-- Qualified Names (Default) ......................... -->

<!ENTITY % SVG.style.qname "style" >

<!-- Attribute Collections (Default) ................... -->

<!ENTITY % SVG.Core.attrib "" >

<!-- SVG.Style.class ................................... -->

<!ENTITY % SVG.Style.extra.class "" >

<!ENTITY % SVG.Style.class
    "| %SVG.style.qname; %SVG.Style.extra.class;"
>

<!-- SVG.Style.attrib .................................. -->

<!ENTITY % SVG.Style.extra.attrib "" >

<!ENTITY % SVG.Style.attrib
    "style %StyleSheet.datatype; #IMPLIED
     class %ClassList.datatype; #IMPLIED
     %SVG.Style.extra.attrib;"
>

<!-- style: Style Element .............................. -->

<!ENTITY % SVG.style.extra.content "" >

<!ENTITY % SVG.style.element "INCLUDE" >
<![%SVG.style.element;[
<!ENTITY % SVG.style.content
    "( #PCDATA %SVG.style.extra.content; )*"
>
<!ELEMENT %SVG.style.qname; %SVG.style.content; >
<!-- end of SVG.style.element -->]]>

<!ENTITY % SVG.style.attlist "INCLUDE" >
<![%SVG.style.attlist;[
<!ATTLIST %SVG.style.qname;
    xml:space ( preserve ) #FIXED 'preserve'
    %SVG.Core.attrib;
    type %ContentType.datatype; #REQUIRED
    media %MediaDesc.datatype; #IMPLIED
    title %Text.datatype; #IMPLIED
>
<!-- end of SVG.style.attlist -->]]>

<!-- end of svg-style.mod -->
