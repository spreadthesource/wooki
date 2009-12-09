<?xml version="1.0" ?>
<xsl:stylesheet version="1.0"
  xmlns:xsl="http://www.w3.org/1999/XSL/Transform"       
  xmlns:fo="http://www.w3.org/1999/XSL/Format"
  xmlns:html="http://www.w3.org/1999/xhtml">
  
  <!-- IdentityTransform -->
  <xsl:template match="/ | @* | node()">
    <xsl:copy>
      <xsl:apply-templates select="@* | node()" />
    </xsl:copy>
  </xsl:template>

<xsl:template match="html:body">
  <xsl:copy>
      <xsl:apply-templates select="@*" />
      <xsl:attribute name="pdftoc">
        <xsl:text>true</xsl:text>
      </xsl:attribute>   
      <html:div id="xwikimaincontainer">
        <xsl:apply-templates select="* | node()" />
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
</xsl:template>

</xsl:stylesheet>

