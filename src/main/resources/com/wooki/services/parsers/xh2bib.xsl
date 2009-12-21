<xsl:transform
 xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
 version="1.0"
 xmlns:h="http://www.w3.org/1999/xhtml"
 >
<!-- $Id: xh2bib.xsl,v 1.5 2006/07/31 13:05:29 connolly Exp $ -->
<!-- see also:
http://www.w3.org/Addressing/url_test/bibtex/bibtex.dtd
Id: bibtex.dtd,v 1.2 1994/04/26 15:19:20 connolly Exp


The Bibtex Bibliographic Format
Last Modified: Summer 1998.
http://www.cc.gatech.edu/classes/RWL/Projects/citation/Docs/UserManuals/Reference_Pages/bibtex_doc.html
-->


<xsl:output method="text"/>

<!-- don't pass text thru -->
<xsl:template match="text()|@*">
</xsl:template>

<!-- don't pay attention to lists without the class <dl class='bib'> -->
<xsl:template match="h:dl">
  <!-- pass -->
</xsl:template>
<xsl:template match="h:ul">
  <!-- pass -->
</xsl:template>
<xsl:template match="h:dl[@class='bib']">
  <xsl:apply-templates/>
</xsl:template>
<!-- ... or <* class='bibliography'> -->
<xsl:template match="h:ul[@class='bibliography']">
  <xsl:for-each select="h:li">
    <xsl:call-template name="bibentry" />
    <xsl:call-template name="bibfields" />
  </xsl:for-each>
</xsl:template>


<xsl:template match="h:dt">
  <xsl:call-template name="bibentry" />
</xsl:template>
<xsl:template match="h:dd">
  <xsl:call-template name="bibfields" />
</xsl:template>

<xsl:template name="bibentry">
 <!-- turn <dt class="Article"><a name="Con04">...</a></dt> into
      @Article{Con04,
   -->
 <xsl:text>@</xsl:text>
 <xsl:value-of select='@class'/>
 <xsl:text>{</xsl:text>
 <xsl:value-of select='concat(@id, h:a/@name)'/>
 <xsl:text>,
</xsl:text>
</xsl:template>

<xsl:template name="bibfields">
 <xsl:call-template name="qfield">
   <xsl:with-param name="f" select='"title"'/>
   <xsl:with-param name="v" select='.//h:cite'/>
 </xsl:call-template>
 
 <xsl:call-template name="cfield">
   <xsl:with-param name="n" select='"author"'/>
 </xsl:call-template>

 <xsl:call-template name="cfield">
   <xsl:with-param name="n" select='"editor"'/>
 </xsl:call-template>

 <xsl:call-template name="cfield">
   <xsl:with-param name="n" select='"journal"'/>
 </xsl:call-template>

 <xsl:call-template name="cfield">
   <xsl:with-param name="n" select='"publisher"'/>
 </xsl:call-template>

 <xsl:call-template name="cfield">
   <xsl:with-param name="n" select='"series"'/>
 </xsl:call-template>

 <xsl:call-template name="cfield">
   <xsl:with-param name="n" select='"booktitle"'/>
 </xsl:call-template>
 
 <xsl:call-template name="cfield">
   <xsl:with-param name="n" select='"type"'/>
 </xsl:call-template>
 
 <xsl:call-template name="cfield">
   <xsl:with-param name="n" select='"institution"'/>
 </xsl:call-template>
 
 <xsl:call-template name="cfield">
   <xsl:with-param name="n" select='"howpublished"'/>
 </xsl:call-template>
 
 <xsl:call-template name="cfield">
   <xsl:with-param name="n" select='"month"'/>
 </xsl:call-template>
 
 <xsl:call-template name="cfield">
   <xsl:with-param name="n" select='"year"'/>
 </xsl:call-template>
 
 <xsl:call-template name="cfield">
   <xsl:with-param name="n" select='"volume"'/>
 </xsl:call-template>
 
 <xsl:call-template name="cfield">
   <xsl:with-param name="n" select='"pages"'/>
 </xsl:call-template>
 
 <xsl:call-template name="cfield">
   <xsl:with-param name="n" select='"chapter"'/>
 </xsl:call-template>
 
 <xsl:call-template name="cfield">
   <xsl:with-param name="n" select='"number"'/>
 </xsl:call-template>
 
 <xsl:call-template name="cfield">
   <xsl:with-param name="n" select='"address"'/>
 </xsl:call-template>
 
 <xsl:if test="h:cite/h:a">
   <!--
     per URLs in BibTeX bibliographies
     http://www.tex.ac.uk/cgi-bin/texfaq2html?label=citeURL
     from a TeX FAQ
   -->
   <xsl:text>howpublished = { \url{</xsl:text>
   <xsl:value-of select='h:cite/h:a/@href'/>
   <xsl:text>} }&#10;</xsl:text>
 </xsl:if>


 <xsl:text>}
</xsl:text>
</xsl:template>

<xsl:template name="cfield">
 <xsl:param name="n"/>

 <xsl:call-template name="field">
   <xsl:with-param name="f" select='$n'/>
   <xsl:with-param name="v" select='*[@class=$n]'/>
 </xsl:call-template>
</xsl:template> 

<xsl:template name="field">
  <xsl:param name="f"/>
  <xsl:param name="v"/>

  <xsl:if test='$v'>
    <xsl:value-of select='$f'/>
    <xsl:text> = {</xsl:text>
    <xsl:apply-templates select='$v' mode="value"/>
    <xsl:text>},
    </xsl:text>
  </xsl:if>
</xsl:template>

<xsl:template name="qfield">
  <xsl:param name="f"/>
  <xsl:param name="v"/>

  <xsl:if test='$v'>
    <xsl:value-of select='$f'/>
    <xsl:text> = "{</xsl:text>
    <xsl:apply-templates select='$v' mode="value"/>
    <xsl:text>}",
    </xsl:text>
  </xsl:if>
</xsl:template>


<xsl:template match="h:span[@class='amp']" mode="value">
  <xsl:text>\&amp;</xsl:text>
</xsl:template>

<!-- kludge for K<span title='\"o'>ö</span>bler -->
<xsl:template match="h:span[@title]" mode="value">
  <xsl:text>{</xsl:text>
  <xsl:value-of select='@title'/>
  <xsl:text>}</xsl:text>
</xsl:template>

<xsl:template match="text()" mode="value">
  <xsl:value-of select='.'/>
</xsl:template>
<xsl:template match="*" mode="value">
  <xsl:apply-templates mode="value"/>
</xsl:template>


</xsl:transform>

