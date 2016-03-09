<!ENTITY % SVG.color-profile.qname "color-profile" >

<!-- Attribute Collections (Default) ................... -->

<!ENTITY % SVG.Core.attrib "" >
<!ENTITY % SVG.XLink.attrib "" >

<!-- SVG.Profile.class ................................. -->

<!ENTITY % SVG.Profile.extra.class "" >

<!ENTITY % SVG.Profile.class
    "| %SVG.color-profile.qname; %SVG.Profile.extra.class;"
>

<!-- SVG.Profile.attrib ................................ -->

<!ENTITY % SVG.Profile.extra.attrib "" >

<!ENTITY % SVG.Profile.attrib
    "color-profile CDATA #IMPLIED
     %SVG.Profile.extra.attrib;"
>

<!-- color-profile: Color Profile Element .............. -->

<!ENTITY % SVG.color-profile.extra.content "" >

<!ENTITY % SVG.color-profile.element "INCLUDE" >
<![%SVG.color-profile.element;[
<!ENTITY % SVG.color-profile.content
    "( %SVG.Description.class; %SVG.color-profile.extra.content; )*"
>
<!ELEMENT %SVG.color-profile.qname; %SVG.color-profile.content; >
<!-- end of SVG.color-profile.element -->]]>

<!ENTITY % SVG.color-profile.attlist "INCLUDE" >
<![%SVG.color-profile.attlist;[
<!ATTLIST %SVG.color-profile.qname;
    %SVG.Core.attrib;
    %SVG.XLink.attrib;
    local CDATA #IMPLIED
    name CDATA #REQUIRED
    rendering-intent ( auto | perceptual | relative-colorimetric | saturation |
                       absolute-colorimetric ) 'auto'
>
<!-- end of SVG.color-profile.attlist -->]]>

<!-- end of svg-profile.mod -->
