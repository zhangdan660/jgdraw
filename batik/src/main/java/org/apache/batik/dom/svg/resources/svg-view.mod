<!ENTITY % SVG.view.qname "view" >

<!-- Attribute Collections (Default) ................... -->

<!ENTITY % SVG.Core.attrib "" >
<!ENTITY % SVG.External.attrib "" >

<!-- SVG.View.class .................................... -->

<!ENTITY % SVG.View.extra.class "" >

<!ENTITY % SVG.View.class
    "| %SVG.view.qname; %SVG.View.extra.class;"
>

<!-- view: View Element ................................ -->

<!ENTITY % SVG.view.extra.content "" >

<!ENTITY % SVG.view.element "INCLUDE" >
<![%SVG.view.element;[
<!ENTITY % SVG.view.content
    "( %SVG.Description.class; %SVG.view.extra.content; )*"
>
<!ELEMENT %SVG.view.qname; %SVG.view.content; >
<!-- end of SVG.view.element -->]]>

<!ENTITY % SVG.view.attlist "INCLUDE" >
<![%SVG.view.attlist;[
<!ATTLIST %SVG.view.qname;
    %SVG.Core.attrib;
    %SVG.External.attrib;
    viewBox %ViewBoxSpec.datatype; #IMPLIED
    preserveAspectRatio %PreserveAspectRatioSpec.datatype; 'xMidYMid meet'
    zoomAndPan ( disable | magnify ) 'magnify'
    viewTarget CDATA #IMPLIED
>
<!-- end of SVG.view.attlist -->]]>

<!-- end of svg-view.mod -->
