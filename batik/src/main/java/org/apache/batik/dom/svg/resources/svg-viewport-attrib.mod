<!ENTITY % ClipValue.datatype "CDATA" >

<!ENTITY % SVG.clip.attrib
    "clip %ClipValue.datatype; #IMPLIED"
>

<!ENTITY % SVG.overflow.attrib
    "overflow ( visible | hidden | scroll | auto | inherit ) #IMPLIED"
>

<!ENTITY % SVG.Viewport.extra.attrib "" >

<!ENTITY % SVG.Viewport.attrib
    "%SVG.clip.attrib;
     %SVG.overflow.attrib;
     %SVG.Viewport.extra.attrib;"
>

<!-- end of svg-viewport-attrib.mod -->
