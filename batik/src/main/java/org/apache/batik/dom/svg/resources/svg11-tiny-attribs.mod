<!ENTITY % ExtensionList.datatype "CDATA" >
<!ENTITY % FeatureList.datatype "CDATA" >

<!ENTITY % SVG.Conditional.extra.attrib "" >
<!ENTITY % SVG.Conditional.attrib
    "requiredFeatures %FeatureList.datatype; #IMPLIED
     requiredExtensions %ExtensionList.datatype; #IMPLIED
     systemLanguage %LanguageCodes.datatype; #IMPLIED
     %SVG.Conditional.extra.attrib;"
>

<!-- module: svg-style.mod ............................. -->

<!ENTITY % ClassList.datatype "CDATA" >
<!ENTITY % StyleSheet.datatype "CDATA" >

<!ENTITY % SVG.Style.extra.attrib "" >
<!ENTITY % SVG.Style.attrib
    "style %StyleSheet.datatype; #IMPLIED
     class %ClassList.datatype; #IMPLIED
     %SVG.Style.extra.attrib;"
>

<!-- module: svg-basic-text.mod ........................ -->

<!ENTITY % FontFamilyValue.datatype "CDATA" >
<!ENTITY % FontSizeValue.datatype "CDATA" >

<!ENTITY % SVG.Font.extra.attrib "" >
<!ENTITY % SVG.Font.attrib
    "font-family %FontFamilyValue.datatype; #IMPLIED
     font-size %FontSizeValue.datatype; #IMPLIED
     font-style ( normal | italic | oblique | inherit ) #IMPLIED
     font-weight ( normal | bold | bolder | lighter | 100 | 200 | 300 | 400 |
                   500 | 600 | 700 | 800 | 900 | inherit ) #IMPLIED
     %SVG.Font.extra.attrib;"
>

<!-- end of svg11-tiny-attribs.mod -->
