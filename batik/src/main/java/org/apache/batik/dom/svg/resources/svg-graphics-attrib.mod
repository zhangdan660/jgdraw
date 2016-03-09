<!ENTITY % SVG.display.attrib
    "display ( inline | block | list-item | run-in | compact | marker |
               table | inline-table | table-row-group | table-header-group |
               table-footer-group | table-row | table-column-group |
               table-column | table-cell | table-caption | none | inherit )
               #IMPLIED"
>

<!ENTITY % SVG.image-rendering.attrib
    "image-rendering ( auto | optimizeSpeed | optimizeQuality | inherit )
                       #IMPLIED"
>

<!ENTITY % SVG.pointer-events.attrib
    "pointer-events ( visiblePainted | visibleFill | visibleStroke | visible |
                      painted | fill | stroke | all | none | inherit )
                      #IMPLIED"
>

<!ENTITY % SVG.shape-rendering.attrib
    "shape-rendering ( auto | optimizeSpeed | crispEdges | geometricPrecision |
                       inherit ) #IMPLIED"
>

<!ENTITY % SVG.text-rendering.attrib
    "text-rendering ( auto | optimizeSpeed | optimizeLegibility |
                      geometricPrecision | inherit ) #IMPLIED"
>

<!ENTITY % SVG.visibility.attrib
    "visibility ( visible | hidden | inherit ) #IMPLIED"
>

<!ENTITY % SVG.Graphics.extra.attrib "" >

<!ENTITY % SVG.Graphics.attrib
    "%SVG.display.attrib;
     %SVG.image-rendering.attrib;
     %SVG.pointer-events.attrib;
     %SVG.shape-rendering.attrib;
     %SVG.text-rendering.attrib;
     %SVG.visibility.attrib;
     %SVG.Graphics.extra.attrib;"
>

<!-- end of svg-graphics-attrib.mod -->
