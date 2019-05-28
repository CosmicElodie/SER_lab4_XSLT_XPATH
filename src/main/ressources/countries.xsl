<?xml version="1.0" encoding="UTF-8" ?>

<xsl:stylesheet
  version   = "1.0"
  xmlns:xsl= "http://www.w3.org/1999/XSL/Transform">


  <xsl:output
    method= "html"
    encoding= "UTF-8"
    indent= "yes"
    />

    <xsl:template match="/">

      <html>
        <head>
          <title>Pays</title>
        </head>
        <body>
          <ul>
            <xsl:for-each select="/countries/element[region='Asia']">
              <xsl:sort select="name" />
              <li>
                <xsl:value-of select="name"/>
                <img width = "20" height = "15"> <xsl:attribute name ="src"> <xsl:value-of select="flag"/> </xsl:attribute>  </img>
              </li>
            </xsl:for-each>

          </ul>
        </body>
      </html>
    </xsl:template>


</xsl:stylesheet>