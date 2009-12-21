<?xml version="1.0" ?>

<xsl:stylesheet version="1.0"
  xmlns:xsl="http://www.w3.org/1999/XSL/Transform"       
  xmlns:fo="http://www.w3.org/1999/XSL/Format"
  xmlns:str="http://xsltsl.org/string"
  xmlns:html="http://www.w3.org/1999/xhtml">
  
  <xsl:import href="./string.xsl"/>

  <!-- IdentityTransform -->
  <xsl:template match="/ | @* | node()">
    <xsl:copy>
      <xsl:apply-templates select="@* | node()" />
    </xsl:copy>
  </xsl:template>

  <xsl:template match="html:title">
    <xsl:copy>
      <xsl:attribute name="class">
        <xsl:text>pdftitle</xsl:text>
      </xsl:attribute>
      <xsl:apply-templates select="@* | node()" />
    </xsl:copy>
    <html:link rel="usepackage" title="url" href="ftp://cam.ctan.org/tex-archive/macros/latex/contrib/misc/url.sty" />
    
  </xsl:template>

<xsl:template match="html:body">
  <xsl:copy>
      <xsl:apply-templates select="@*" />
      <xsl:attribute name="pdftoc">
        <xsl:text>true</xsl:text>
      </xsl:attribute>   
      <xsl:attribute name="pdfcover">
        <xsl:text>1</xsl:text>
      </xsl:attribute>   
      <html:div class="maketitle">
        <html:h1>
          <xsl:copy-of select="preceding::html:head/html:title"/>
        </html:h1>
          <html:h2>
            <!--html:img src="http://farm3.static.flickr.com/2200/2238010657_da9d32012e.jpg"/-->
            <xsl:variable name="authors">
              <xsl:for-each select="preceding::html:head/html:link[@rev='MADE']/@title">
                <xsl:value-of select="."/><xsl:text>, </xsl:text>
              </xsl:for-each>
            </xsl:variable>
            
            <xsl:value-of select="substring($authors, 1, string-length($authors) - 2)"/>
            <xsl:variable name="last-author" select="preceding::html:head/html:link[@rev='MADE'][position()=last()]/@title"/>
            <!--xsl:message>Last author vaut: <xsl:value-of select="$last-author"/> et son decoupage vaut <xsl:value-of select="substring($last-author, string-length($last-author))"/></xsl:message-->
            <xsl:if test="substring($last-author, string-length($last-author)) != '.'">
              <xsl:text>.</xsl:text>
            </xsl:if>
          </html:h2>
      </html:div>
      <html:div id="xwikimaincontainer">
        <xsl:apply-templates select="* | node()" />
      </html:div>
      <html:div class="pdffooter">
        <span><xsl:copy-of select="preceding::html:head/html:title"/></span>
      </html:div>
  </xsl:copy>

</xsl:template>

<xsl:template match="html:h1[position()=1]">

    <xsl:copy>
      <xsl:apply-templates select="@*" />
      <xsl:attribute name="class">
        <xsl:text>pdftoc</xsl:text>
      </xsl:attribute>  
      <xsl:apply-templates select="* | node()" />
    </xsl:copy>
</xsl:template>

<xsl:template match="html:h1|html:h2|html:h3|html:h4|html:h5|html:h6">
    <xsl:copy>
      <xsl:apply-templates select="@*" />
      <xsl:attribute name="class">
        <xsl:text>pdftoc</xsl:text>
      </xsl:attribute>  
      <xsl:apply-templates select="* | node()" />
    </xsl:copy>
      <!--html:div class="pdffooter">
        <span><xsl:copy-of select="text()"/></span>
      </html:div-->
</xsl:template>

</xsl:stylesheet>

