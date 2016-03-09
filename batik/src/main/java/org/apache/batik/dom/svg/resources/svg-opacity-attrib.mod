<!ENTITY % SVG.opacity.attrib
    "opacity %OpacityValue.datatype; #IMPLIED"
>

<!ENTITY % SVG.fill-opacity.attrib
    "fill-opacity %OpacityValue.datatype; #IMPLIED"
>

<!ENTITY % SVG.stroke-opacity.attrib
    "stroke-opacity %OpacityValue.datatype; #IMPLIED"
>

<!ENTITY % SVG.Opacity.extra.attrib "" >

<!ENTITY % SVG.Opacity.attrib
    "%SVG.opacity.attrib;
     %SVG.fill-opacity.attrib;
     %SVG.stroke-opacity.attrib;
     %SVG.Opacity.extra.attrib;"
>

<!-- end of svg-opacity-attrib.mod -->
