<!ENTITY % SVG.image.qname "image" >

<!-- Attribute Collections (Default) ................... -->

<!ENTITY % SVG.Core.attrib "" >
<!ENTITY % SVG.Conditional.attrib "" >
<!ENTITY % SVG.Style.attrib "" >
<!ENTITY % SVG.Viewport.attrib "" >
<!ENTITY % SVG.Color.attrib "" >
<!ENTITY % SVG.Opacity.attrib "" >
<!ENTITY % SVG.Graphics.attrib "" >
<!ENTITY % SVG.Profile.attrib "" >
<!ENTITY % SVG.Clip.attrib "" >
<!ENTITY % SVG.Mask.attrib "" >
<!ENTITY % SVG.Filter.attrib "" >
<!ENTITY % SVG.GraphicalEvents.attrib "" >
<!ENTITY % SVG.Cursor.attrib "" >
<!ENTITY % SVG.XLinkEmbed.attrib "" >
<!ENTITY % SVG.External.attrib "" >

<!-- SVG.Image.class ................................... -->

<!ENTITY % SVG.Image.extra.class "" >

<!ENTITY % SVG.Image.class
    "| %SVG.image.qname; %SVG.Image.extra.class;"
>

<!-- image: Image Element .............................. -->

<!ENTITY % SVG.image.extra.content "" >

<!ENTITY % SVG.image.element "INCLUDE" >
<![%SVG.image.element;[
<!ENTITY % SVG.image.content
    "(( %SVG.Description.class; )*, ( %SVG.Animation.class;
        %SVG.image.extra.content; )*)"
>
<!ELEMENT %SVG.image.qname; %SVG.image.content; >
<!-- end of SVG.image.element -->]]>

<!ENTITY % SVG.image.attlist "INCLUDE" >
<![%SVG.image.attlist;[
<!ATTLIST %SVG.image.qname;
    %SVG.Core.attrib;
    %SVG.Conditional.attrib;
    %SVG.Style.attrib;
    %SVG.Viewport.attrib;
    %SVG.Color.attrib;
    %SVG.Opacity.attrib;
    %SVG.Graphics.attrib;
    %SVG.Profile.attrib;
    %SVG.Clip.attrib;
    %SVG.Mask.attrib;
    %SVG.Filter.attrib;
    %SVG.GraphicalEvents.attrib;
    %SVG.Cursor.attrib;
    %SVG.XLinkEmbed.attrib;
    %SVG.External.attrib;
    x %Coordinate.datatype; #IMPLIED
    y %Coordinate.datatype; #IMPLIED
    width %Length.datatype; #REQUIRED
    height %Length.datatype; #REQUIRED
    preserveAspectRatio %PreserveAspectRatioSpec.datatype; 'xMidYMid meet'
    transform %TransformList.datatype; #IMPLIED
>
<!-- end of SVG.image.attlist -->]]>

<!-- end of svg-image.mod -->
