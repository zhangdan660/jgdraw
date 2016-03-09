<!ENTITY % SVG.XLink.extra.attrib "" >

<!ENTITY % SVG.XLink.attrib
    "%XLINK.xmlns.attrib;
     %XLINK.pfx;type ( simple ) #FIXED 'simple'
     %XLINK.pfx;href %URI.datatype; #IMPLIED
     %XLINK.pfx;role %URI.datatype; #IMPLIED
     %XLINK.pfx;arcrole %URI.datatype; #IMPLIED
     %XLINK.pfx;title CDATA #IMPLIED
     %XLINK.pfx;show ( other ) 'other'
     %XLINK.pfx;actuate ( onLoad ) #FIXED 'onLoad'
     %SVG.XLink.extra.attrib;"
>

<!ENTITY % SVG.XLinkRequired.extra.attrib "" >

<!ENTITY % SVG.XLinkRequired.attrib
    "%XLINK.xmlns.attrib;
     %XLINK.pfx;type ( simple ) #FIXED 'simple'
     %XLINK.pfx;href %URI.datatype; #REQUIRED
     %XLINK.pfx;role %URI.datatype; #IMPLIED
     %XLINK.pfx;arcrole %URI.datatype; #IMPLIED
     %XLINK.pfx;title CDATA #IMPLIED
     %XLINK.pfx;show ( other ) 'other'
     %XLINK.pfx;actuate ( onLoad ) #FIXED 'onLoad'
     %SVG.XLinkRequired.extra.attrib;"
>

<!ENTITY % SVG.XLinkEmbed.extra.attrib "" >

<!ENTITY % SVG.XLinkEmbed.attrib
    "%XLINK.xmlns.attrib;
     %XLINK.pfx;type ( simple ) #FIXED 'simple'
     %XLINK.pfx;href %URI.datatype; #REQUIRED
     %XLINK.pfx;role %URI.datatype; #IMPLIED
     %XLINK.pfx;arcrole %URI.datatype; #IMPLIED
     %XLINK.pfx;title CDATA #IMPLIED
     %XLINK.pfx;show ( embed ) 'embed'
     %XLINK.pfx;actuate ( onLoad ) #FIXED 'onLoad'
     %SVG.XLinkEmbed.extra.attrib;"
>

<!ENTITY % SVG.XLinkReplace.extra.attrib "" >

<!ENTITY % SVG.XLinkReplace.attrib
    "%XLINK.xmlns.attrib;
     %XLINK.pfx;type ( simple ) #FIXED 'simple'
     %XLINK.pfx;href %URI.datatype; #REQUIRED
     %XLINK.pfx;role %URI.datatype; #IMPLIED
     %XLINK.pfx;arcrole %URI.datatype; #IMPLIED
     %XLINK.pfx;title CDATA #IMPLIED
     %XLINK.pfx;show ( new | replace ) 'replace'
     %XLINK.pfx;actuate ( onRequest ) #FIXED 'onRequest'
     %SVG.XLinkReplace.extra.attrib;"
>

<!-- end of svg-xlink-attrib.mod -->
