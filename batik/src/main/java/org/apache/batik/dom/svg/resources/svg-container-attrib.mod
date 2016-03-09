<!-- 'enable-background' property/attribute value (e.g., 'new', 'accumulate') -->
<!ENTITY % EnableBackgroundValue.datatype "CDATA" >

<!ENTITY % SVG.enable-background.attrib
    "enable-background %EnableBackgroundValue.datatype; #IMPLIED"
>

<!ENTITY % SVG.Container.extra.attrib "" >

<!ENTITY % SVG.Container.attrib
    "%SVG.enable-background.attrib;
     %SVG.Container.extra.attrib;"
>

<!-- end of svg-container-attrib.mod -->
