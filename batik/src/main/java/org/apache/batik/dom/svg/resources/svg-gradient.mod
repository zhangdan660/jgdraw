<!ENTITY % NumberOrPercentage.datatype "CDATA" >

<!-- Qualified Names (Default) ......................... -->

<!ENTITY % SVG.linearGradient.qname "linearGradient" >
<!ENTITY % SVG.radialGradient.qname "radialGradient" >
<!ENTITY % SVG.stop.qname "stop" >

<!-- Attribute Collections (Default) ................... -->

<!ENTITY % SVG.Core.attrib "" >
<!ENTITY % SVG.Style.attrib "" >
<!ENTITY % SVG.Color.attrib "" >
<!ENTITY % SVG.XLink.attrib "" >
<!ENTITY % SVG.External.attrib "" >

<!-- SVG.Gradient.class ................................ -->

<!ENTITY % SVG.Gradient.extra.class "" >

<!ENTITY % SVG.Gradient.class
    "| %SVG.linearGradient.qname; | %SVG.radialGradient.qname;
       %SVG.Gradient.extra.class;"
>

<!-- SVG.Gradient.attrib ............................... -->

<!ENTITY % SVG.Gradient.extra.attrib "" >

<!ENTITY % SVG.Gradient.attrib
    "stop-color %SVGColor.datatype; #IMPLIED
     stop-opacity %OpacityValue.datatype; #IMPLIED
     %SVG.Gradient.extra.attrib;"
>

<!-- linearGradient: Linear Gradient Element ........... -->

<!ENTITY % SVG.linearGradient.extra.content "" >

<!ENTITY % SVG.linearGradient.element "INCLUDE" >
<![%SVG.linearGradient.element;[
<!ENTITY % SVG.linearGradient.content
    "(( %SVG.Description.class; )*, ( %SVG.stop.qname; | %SVG.animate.qname;
      | %SVG.set.qname; | %SVG.animateTransform.qname;
        %SVG.linearGradient.extra.content; )*)"
>
<!ELEMENT %SVG.linearGradient.qname; %SVG.linearGradient.content; >
<!-- end of SVG.linearGradient.element -->]]>

<!ENTITY % SVG.linearGradient.attlist "INCLUDE" >
<![%SVG.linearGradient.attlist;[
<!ATTLIST %SVG.linearGradient.qname;
    %SVG.Core.attrib;
    %SVG.Style.attrib;
    %SVG.Color.attrib;
    %SVG.Gradient.attrib;
    %SVG.XLink.attrib;
    %SVG.External.attrib;
    x1 %Coordinate.datatype; #IMPLIED
    y1 %Coordinate.datatype; #IMPLIED
    x2 %Coordinate.datatype; #IMPLIED
    y2 %Coordinate.datatype; #IMPLIED
    gradientUnits ( userSpaceOnUse | objectBoundingBox ) #IMPLIED
    gradientTransform %TransformList.datatype; #IMPLIED
    spreadMethod ( pad | reflect | repeat ) #IMPLIED
>
<!-- end of SVG.linearGradient.attlist -->]]>

<!-- radialGradient: Radial Gradient Element ........... -->

<!ENTITY % SVG.radialGradient.extra.content "" >

<!ENTITY % SVG.radialGradient.element "INCLUDE" >
<![%SVG.radialGradient.element;[
<!ENTITY % SVG.radialGradient.content
    "(( %SVG.Description.class; )*, ( %SVG.stop.qname; | %SVG.animate.qname;
      | %SVG.set.qname; | %SVG.animateTransform.qname;
        %SVG.radialGradient.extra.content; )*)"
>
<!ELEMENT %SVG.radialGradient.qname; %SVG.radialGradient.content; >
<!-- end of SVG.radialGradient.element -->]]>

<!ENTITY % SVG.radialGradient.attlist "INCLUDE" >
<![%SVG.radialGradient.attlist;[
<!ATTLIST %SVG.radialGradient.qname;
    %SVG.Core.attrib;
    %SVG.Style.attrib;
    %SVG.Color.attrib;
    %SVG.Gradient.attrib;
    %SVG.XLink.attrib;
    %SVG.External.attrib;
    cx %Coordinate.datatype; #IMPLIED
    cy %Coordinate.datatype; #IMPLIED
    r %Length.datatype; #IMPLIED
    fx %Coordinate.datatype; #IMPLIED
    fy %Coordinate.datatype; #IMPLIED
    gradientUnits ( userSpaceOnUse | objectBoundingBox ) #IMPLIED
    gradientTransform %TransformList.datatype; #IMPLIED
    spreadMethod ( pad | reflect | repeat ) #IMPLIED
>
<!-- end of SVG.radialGradient.attlist -->]]>

<!-- stop: Stop Element ................................ -->

<!ENTITY % SVG.stop.extra.content "" >

<!ENTITY % SVG.stop.element "INCLUDE" >
<![%SVG.stop.element;[
<!ENTITY % SVG.stop.content
    "( %SVG.animate.qname; | %SVG.set.qname; | %SVG.animateColor.qname;
       %SVG.stop.extra.content; )*"
>
<!ELEMENT %SVG.stop.qname; %SVG.stop.content; >
<!-- end of SVG.stop.element -->]]>

<!ENTITY % SVG.stop.attlist "INCLUDE" >
<![%SVG.stop.attlist;[
<!ATTLIST %SVG.stop.qname;
    %SVG.Core.attrib;
    %SVG.Style.attrib;
    %SVG.Color.attrib;
    %SVG.Gradient.attrib;
    offset %NumberOrPercentage.datatype; #REQUIRED
>
<!-- end of SVG.stop.attlist -->]]>

<!-- end of svg-gradient.mod -->
