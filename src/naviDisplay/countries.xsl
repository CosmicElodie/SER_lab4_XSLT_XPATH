<?xml version="1.0" encoding="UTF-8" standalone="no"?><xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
<xsl:template match="/">

<html>
  <head>
    <!-- Bootstrap stylesheet and javascript -->
    <script src="js/jquery-3.4.1.min.js"/>
    <script src="js/bootstrap.min.js"/>
    <link href="css/bootstrap.min.css" rel="stylesheet"/>
  </head>
<body>
  <div class="row">
    <xsl:for-each select="countries/element">
      <div class="col-2">
      <button class="btn btn-light" data-toggle="modal" type="button">
        <xsl:attribute name="data-target">#modal<xsl:value-of select="numericCode"/></xsl:attribute>
        <xsl:value-of select="name"/>
        <xsl:element name="img">
          <xsl:attribute name="src"><xsl:value-of select="flag"/></xsl:attribute>
          <xsl:attribute name="height"><xsl:value-of select="20"/></xsl:attribute>
          <xsl:attribute name="width"><xsl:value-of select="40"/></xsl:attribute>
        </xsl:element>
      </button>
    </div>

      <!-- Modal -->
      <div aria-hidden="true" aria-labelledby="exampleModalLabel" class="modal fade" role="dialog" tabindex="-1">
        <xsl:attribute name="id">modal<xsl:value-of select="numericCode"/></xsl:attribute>
        <div class="modal-dialog" role="document">
          <div class="modal-content">
            <div class="modal-header">
              <h5 class="modal-title" id="exampleModalLabel"><xsl:value-of select="name"/></h5>
              <button aria-label="Close" class="close" data-dismiss="modal" type="button">

              </button>
            </div>
            <div class="modal-body">
              <div class="row">
                <div style="margin-left: 2%; width: 48%">
                  <xsl:element name="img">
                    <xsl:attribute name="src"><xsl:value-of select="flag"/></xsl:attribute>
                    <xsl:attribute name="height"><xsl:value-of select="100"/></xsl:attribute>
                    <xsl:attribute name="width"><xsl:value-of select="200"/></xsl:attribute>
                  </xsl:element>
                </div>
                <div style="margin-right: 2%; width: 48%">
                  <p>Capitale : <xsl:value-of select="capital"/></p>
                  <p>Population : <xsl:value-of select="population"/> habitants</p>
                  <p>Superficie : <xsl:value-of select="area"/> km2</p>
                  <p>Continent : <xsl:value-of select="region"/> </p>
                  <p>Sous-continent : <xsl:value-of select="subregion"/> </p>
                </div>
              </div>

              <div class="card">
                <div class="card-header">
                  Languages
                </div>
                <ul class="list-group list-group-flush">
                  <xsl:for-each select="./languages/element">
                    <li class="list-group-item"><xsl:value-of select="name"/></li>
                  </xsl:for-each>
                </ul>
              </div>
            </div>
            <div class="modal-footer">
              <button class="btn btn-secondary" data-dismiss="modal" type="button">Fermer</button>
            </div>
          </div>
        </div>
      </div>
    </xsl:for-each>
  </div>
</body>
</html>
</xsl:template>
</xsl:stylesheet>