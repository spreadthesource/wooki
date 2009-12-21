<?xml version="1.0" encoding="ISO-8859-1"?>

<xsl:stylesheet version="1.0" 
  xmlns:fo="http://www.w3.org/1999/XSL/Format"
  xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
  xmlns:html="http://www.w3.org/1999/xhtml"
  xmlns:str="http://xsltsl.org/string"
  >

  <xsl:import href="./string.xsl"/>
  <xsl:output method="xml"
    version="1.0" indent="yes"
    encoding="UTF-8"/>

    <!-- attributes -->
 
    <!-- page size -->
    <xsl:param name="page-width">8.5in</xsl:param>
    <xsl:variable name="html-page-width" select="html:html/html:body/@page-width"/>
    <xsl:param name="page-height">11in</xsl:param>
    <xsl:variable name="html-page-height" select="html:html/html:body/@page-height"/>
    <xsl:param name="page-margin-top">15mm</xsl:param>
    <xsl:variable name="html-page-margin-top" select="html:html/html:body/@page-margin-top"/>
    <xsl:param name="page-margin-bottom">15mm</xsl:param>
    <xsl:variable name="html-page-margin-bottom" select="html:html/html:body/@page-margin-bottom"/>
    <xsl:param name="page-margin-left">20mm</xsl:param>
    <xsl:variable name="html-page-margin-left" select="html:html/html:body/@page-margin-left"/>
    <xsl:param name="page-margin-right">20mm</xsl:param>
    <xsl:variable name="html-page-margin-right" select="html:html/html:body/@page-margin-right"/>

    <!-- page header and hooter extent  -->
    <xsl:param name="page-header-extent">13mm</xsl:param>
    <xsl:variable name="html-page-header-extent" select="html:html/html:body/html:page-header/@page-header-extent"/>
    <xsl:param name="page-footer-extent">13mm</xsl:param>
    <xsl:variable name="html-page-footer-extent" select="html:html/html:body/html:page-footer/@page-footer-extent"/>

    <!-- printing item in header and footer -->
    <xsl:param name="title-print-in-header" select="true()"/>
    <xsl:variable name="html-title-print-in-header" select="html:html/html:body/html:page-header/@title-print-in-header"/>
    <xsl:param name="page-number-print-in-footer" select="true()"/>
    <xsl:variable name="html-page-number-print-in-footer" select="html:html/html:body/html:page-header/@page-number-print-in-footer"/>

    <!-- page size -->
    <xsl:variable name="calculated-page-width">
      <xsl:choose>
        <!-- No customization found in input XHTML file -->
        <xsl:when test="string($html-page-width)=''">
          <xsl:value-of select="$page-width"/>
        </xsl:when>
        <xsl:otherwise>
          <xsl:value-of select="$html-page-width"/>
        </xsl:otherwise>
      </xsl:choose>
    </xsl:variable>
    <xsl:variable name="calculated-page-height">
      <xsl:choose>
        <!-- No customization found in input XHTML file -->
        <xsl:when test="string($html-page-height)=''">
          <xsl:value-of select="$page-height"/>
        </xsl:when>
        <xsl:otherwise>
          <xsl:value-of select="$html-page-height"/>
        </xsl:otherwise>
      </xsl:choose>
    </xsl:variable>
    
    <xsl:variable name="calculated-page-margin-top">
      <xsl:choose>
        <!-- No customization found in input XHTML file -->
        <xsl:when test="string($html-page-margin-top)=''">
          <xsl:value-of select="$page-margin-top"/>
        </xsl:when>
        <xsl:otherwise>
          <xsl:value-of select="$html-page-margin-top"/>
        </xsl:otherwise>
      </xsl:choose>
    </xsl:variable>
    <xsl:variable name="calculated-page-margin-bottom">
      <xsl:choose>
        <!-- No customization found in input XHTML file -->
        <xsl:when test="string($html-page-margin-bottom)=''">
          <xsl:value-of select="$page-margin-bottom"/>
        </xsl:when>
        <xsl:otherwise>
          <xsl:value-of select="$html-page-margin-bottom"/>
        </xsl:otherwise>
      </xsl:choose>
    </xsl:variable>
    <xsl:variable name="calculated-page-margin-left">
      <xsl:choose>
        <!-- No customization found in input XHTML file -->
        <xsl:when test="string($html-page-margin-left)=''">
          <xsl:value-of select="$page-margin-left"/>
        </xsl:when>
        <xsl:otherwise>
          <xsl:value-of select="$html-page-margin-left"/>
        </xsl:otherwise>
      </xsl:choose>
    </xsl:variable>
    <xsl:variable name="calculated-page-margin-right">
      <xsl:choose>
        <!-- No customization found in input XHTML file -->
        <xsl:when test="string($html-page-margin-right)=''">
          <xsl:value-of select="$page-margin-right"/>
        </xsl:when>
        <xsl:otherwise>
          <xsl:value-of select="$html-page-margin-right"/>
        </xsl:otherwise>
      </xsl:choose>
    </xsl:variable>

    <!-- page header and hooter extent  -->
    <xsl:variable name="calculated-page-header-extent">
      <xsl:choose>
        <!-- No customization found in input XHTML file -->
        <xsl:when test="string($html-page-header-extent)=''">
          <xsl:value-of select="$page-header-extent"/>
        </xsl:when>
        <xsl:otherwise>
          <xsl:value-of select="$html-page-header-extent"/>
        </xsl:otherwise>
      </xsl:choose>
    </xsl:variable>
    <xsl:variable name="calculated-page-footer-extent">
      <xsl:choose>
        <!-- No customization found in input XHTML file -->
        <xsl:when test="string($html-page-footer-extent)=''">
          <xsl:value-of select="$page-footer-extent"/>
        </xsl:when>
        <xsl:otherwise>
          <xsl:value-of select="$html-page-footer-extent"/>
        </xsl:otherwise>
      </xsl:choose>
    </xsl:variable>

    <!-- printing item in header and footer -->
    <xsl:variable name="calculated-title-print-in-header">
      <xsl:choose>
        <!-- No customization found in input XHTML file -->
        <xsl:when test="string($html-title-print-in-header)=''">
          <xsl:value-of select="$title-print-in-header"/>
        </xsl:when>
        <xsl:otherwise>
          <xsl:value-of select="$html-title-print-in-header"/>
        </xsl:otherwise>
      </xsl:choose>
    </xsl:variable>
    <xsl:variable name="calculated-page-number-print-in-footer">
      <xsl:choose>
        <!-- No customization found in input XHTML file -->
        <xsl:when test="string($html-page-number-print-in-footer)=''">
          <xsl:value-of select="$page-number-print-in-footer"/>
        </xsl:when>
        <xsl:otherwise>
          <xsl:value-of select="$html-page-number-print-in-footer"/>
        </xsl:otherwise>
      </xsl:choose>
    </xsl:variable>


    <xsl:template match="/">
      <xsl:apply-templates select="@* | * | comment() | processing-instruction()"/>
    </xsl:template>

   <xsl:template   match="html:html">
			<xsl:param name="is-in-header-or-footer">false</xsl:param>
     <!--xsl:message>
       <xsl:value-of select="document('model://xsl/faxop/xhtml1-transitional.dtd')"/>
     </xsl:message-->
     <!--xsl:text><![CDATA[<]]>!DOCTYPE fo:root [
      <![CDATA[<]]>!ENTITY nbsp "&#x164;">
       ]></xsl:text-->
      
      <fo:root>
      <xsl:if test="@lang">
      <xsl:attribute name="xml:lang"><xsl:value-of select="@lang" /></xsl:attribute>
      </xsl:if>
      <xsl:if test="@xml:lang">
      <xsl:attribute name="xml:lang"><xsl:value-of select="@xml:lang" /></xsl:attribute>
      </xsl:if>
      <xsl:call-template  name="layout-master-set"/>
      <xsl:apply-templates select="html:body"/>
      </fo:root>
   </xsl:template>
   
   <!-- Headings -->
   <xsl:attribute-set name="h1">
        <xsl:attribute name="space-before">1em</xsl:attribute>
        <xsl:attribute name="space-after">0.5em</xsl:attribute>
        <xsl:attribute name="font-size">xx-large</xsl:attribute>
        <xsl:attribute name="font-weight">bold</xsl:attribute>
        <xsl:attribute name="color">black</xsl:attribute>
        <xsl:attribute name="keep-with-next">always</xsl:attribute>
    </xsl:attribute-set>
    <xsl:attribute-set name="h2">
        <xsl:attribute name="space-before">1em</xsl:attribute>
        <xsl:attribute name="space-after">0.5em</xsl:attribute>
        <xsl:attribute name="font-size">x-large</xsl:attribute>
        <xsl:attribute name="font-weight">bold</xsl:attribute>
        <xsl:attribute name="color">black</xsl:attribute>
        <xsl:attribute name="keep-with-next">always</xsl:attribute>
    </xsl:attribute-set>
    <xsl:attribute-set name="h3">
        <xsl:attribute name="space-before">1em</xsl:attribute>
        <xsl:attribute name="space-after">0.5em</xsl:attribute>
        <xsl:attribute name="font-size">large</xsl:attribute>
        <xsl:attribute name="font-weight">bold</xsl:attribute>
        <xsl:attribute name="color">black</xsl:attribute>
        <xsl:attribute name="keep-with-next">always</xsl:attribute>
    </xsl:attribute-set>
    <xsl:attribute-set name="h4">
        <xsl:attribute name="space-before">1em</xsl:attribute>
        <xsl:attribute name="space-after">0.5em</xsl:attribute>
        <xsl:attribute name="font-size">medium</xsl:attribute>
        <xsl:attribute name="font-weight">bold</xsl:attribute>
        <xsl:attribute name="color">black</xsl:attribute>
        <xsl:attribute name="keep-with-next">always</xsl:attribute>
    </xsl:attribute-set>
    <xsl:attribute-set name="h5">
        <xsl:attribute name="space-before">1em</xsl:attribute>
        <xsl:attribute name="space-after">0.5em</xsl:attribute>
        <xsl:attribute name="font-size">small</xsl:attribute>
        <xsl:attribute name="font-weight">bold</xsl:attribute>
        <xsl:attribute name="color">black</xsl:attribute>
        <xsl:attribute name="keep-with-next">always</xsl:attribute>
    </xsl:attribute-set>
    <xsl:attribute-set name="h6">
        <xsl:attribute name="space-before">1em</xsl:attribute>
        <xsl:attribute name="space-after">0.5em</xsl:attribute>
        <xsl:attribute name="font-size">x-small</xsl:attribute>
        <xsl:attribute name="font-weight">bold</xsl:attribute>
        <xsl:attribute name="color">black</xsl:attribute>
        <xsl:attribute name="keep-with-next">always</xsl:attribute>
    </xsl:attribute-set>
    
    <!-- Block level Attribute -->
    <!-- p-->
    <xsl:attribute-set name="p">
    	<xsl:attribute name="text-align">justify</xsl:attribute>
        <xsl:attribute name="text-indent">1em</xsl:attribute>
        <xsl:attribute name="space-before">0.6em</xsl:attribute>
        <xsl:attribute name="space-after">0.6em</xsl:attribute>
        <xsl:attribute name="font-size">10pt</xsl:attribute>
    </xsl:attribute-set>
    
    <!-- pre -->
    <xsl:attribute-set name="pre">
        <xsl:attribute name="font-family">monospace</xsl:attribute>
        <xsl:attribute name="white-space">pre</xsl:attribute>
        <xsl:attribute name="wrap-option">wrap</xsl:attribute>
        <xsl:attribute name="text-indent">1em</xsl:attribute>
        <xsl:attribute name="space-before">0.6em</xsl:attribute>
        <xsl:attribute name="space-after">0.6em</xsl:attribute>
    </xsl:attribute-set>
    <!-- blockquote -->
    <xsl:attribute-set name="blockquote">
        <xsl:attribute name="start-indent">inherit + 4em </xsl:attribute>
        <xsl:attribute name="end-indent">inherit + 4em</xsl:attribute>
        <xsl:attribute name="text-indent">1em</xsl:attribute>
        <xsl:attribute name="space-before">0.6em</xsl:attribute>
        <xsl:attribute name="space-after">0.6em</xsl:attribute>
        <xsl:attribute name="margin-top">1em</xsl:attribute>
        <xsl:attribute name="margin-bottom">1em</xsl:attribute>
    </xsl:attribute-set>
    <!-- address -->
    <xsl:attribute-set name="address">
        <xsl:attribute name="font-style">italic</xsl:attribute>
    </xsl:attribute-set>
    
    <!-- ul -->
    <!-- for list-block -->
    <xsl:attribute-set name="ul">
        <xsl:attribute name="provisional-distance-between-starts">1em</xsl:attribute>
        <xsl:attribute name="provisional-label-separation">1em</xsl:attribute>
    </xsl:attribute-set>

    <xsl:attribute-set name="ul-li">
    </xsl:attribute-set>
    
    <!-- ol -->
    <!-- for list-block -->
    <xsl:attribute-set name="ol">
        <xsl:attribute name="provisional-distance-between-starts">1em</xsl:attribute>
        <xsl:attribute name="provisional-label-separation">1em</xsl:attribute>
    </xsl:attribute-set>
    <xsl:attribute-set name="ol-li">
    </xsl:attribute-set>
    <!-- dl dt dd -->
    <xsl:attribute-set name="dl">
    </xsl:attribute-set>
    <xsl:attribute-set name="dt">
    </xsl:attribute-set>
    <xsl:attribute-set name="dd">
        <xsl:attribute name="start-indent">inherit +1em</xsl:attribute>
        <xsl:attribute name="space-before">0.6em</xsl:attribute>
        <xsl:attribute name="space-after">0.6em</xsl:attribute>
 
    </xsl:attribute-set>

    <!-- Text -->
    <xsl:attribute-set name="em">
        <xsl:attribute name="font-style">italic</xsl:attribute>
        </xsl:attribute-set>
    <xsl:attribute-set name="strong">
        <xsl:attribute name="font-weight">bolder</xsl:attribute>
    </xsl:attribute-set>
    <xsl:attribute-set name="cite">
        <xsl:attribute name="font-style">italic</xsl:attribute>
    </xsl:attribute-set>
    <xsl:attribute-set name="dfn">
        <xsl:attribute name="font-style">italic</xsl:attribute>
    </xsl:attribute-set>
    <xsl:attribute-set name="code">
        <xsl:attribute name="font-family">monospace</xsl:attribute>
    </xsl:attribute-set>

    <xsl:attribute-set name="samp">
        <xsl:attribute name="font-family">monospace</xsl:attribute>
    </xsl:attribute-set>

    <xsl:attribute-set name="kbd">
        <xsl:attribute name="font-family">monospace</xsl:attribute>
    </xsl:attribute-set>

    <xsl:attribute-set name="var">
        <xsl:attribute name="font-style">italic</xsl:attribute>
    </xsl:attribute-set>

    <xsl:attribute-set name="abbr">
    </xsl:attribute-set>

    <xsl:attribute-set name="acronym">
    </xsl:attribute-set>


    <xsl:attribute-set name="sup">
        <xsl:attribute name="baseline-shift">super</xsl:attribute>
        <xsl:attribute name="font-size">.83em</xsl:attribute>
    </xsl:attribute-set>


    <xsl:attribute-set name="sub">
        <xsl:attribute name="baseline-shift">sub</xsl:attribute>
        <xsl:attribute name="font-size">.83em</xsl:attribute>
    </xsl:attribute-set>

    <xsl:attribute-set name="a">
        <xsl:attribute name="text-decoration">underline</xsl:attribute>
        <xsl:attribute name="color">blue</xsl:attribute>
    </xsl:attribute-set>

    <xsl:attribute-set name="div">
        <xsl:attribute name="start-indent">5mm</xsl:attribute>
        <xsl:attribute name="end-indent">5mm</xsl:attribute>
        <xsl:attribute name="font-size">10pt</xsl:attribute>
    </xsl:attribute-set>

    <!-- Table -->
   
    <xsl:attribute-set name="table">
    </xsl:attribute-set>
    
    <xsl:attribute-set name="table-caption">
        <xsl:attribute name="text-align">center</xsl:attribute>
    </xsl:attribute-set>

    <xsl:attribute-set name="tr">
    </xsl:attribute-set>
    
    <xsl:attribute-set name="th">
      <xsl:attribute name="background-color">#DDDDDD</xsl:attribute>
      <xsl:attribute name="border-style">solid</xsl:attribute>
      <xsl:attribute name="border-width">1pt</xsl:attribute>
      <xsl:attribute name="padding-start">0.3em</xsl:attribute>
      <xsl:attribute name="padding-end">0.2em</xsl:attribute>
      <xsl:attribute name="padding-before">2pt</xsl:attribute>
      <xsl:attribute name="padding-end">2pt</xsl:attribute>
    </xsl:attribute-set>
    
    <xsl:attribute-set name="td">
      <xsl:attribute name="border-style">none</xsl:attribute> <!-- solid -->
      <xsl:attribute name="border-width">1pt</xsl:attribute>
      <xsl:attribute name="padding-start">0.3em</xsl:attribute>
      <xsl:attribute name="padding-end">0.2em</xsl:attribute>
      <xsl:attribute name="padding-before">2pt</xsl:attribute>
      <xsl:attribute name="padding-end">2pt</xsl:attribute>
    </xsl:attribute-set>



   <!-- Head -->
   <xsl:template   match="html:head">
			<xsl:param name="is-in-header-or-footer">false</xsl:param>
   </xsl:template>

  
   <xsl:template   match="html:title">
			<xsl:param name="is-in-header-or-footer">false</xsl:param>
      <xsl:apply-templates />
   </xsl:template>

  
   <xsl:template   match="html:base">
			<xsl:param name="is-in-header-or-footer">false</xsl:param>
      <xsl:apply-templates />
   </xsl:template>

  
   <xsl:template   match="html:meta">
			<xsl:param name="is-in-header-or-footer">false</xsl:param>
      <xsl:apply-templates />
   </xsl:template>

  
   <xsl:template   match="html:link">
      <xsl:apply-templates />
   </xsl:template>

  
   <xsl:template   match="html:style">
			<xsl:param name="is-in-header-or-footer">false</xsl:param>
      <xsl:apply-templates />
   </xsl:template>

  
   <xsl:template   match="html:script">
			<xsl:param name="is-in-header-or-footer">false</xsl:param>
      <xsl:apply-templates />
   </xsl:template>

  
   <xsl:template   match="html:noscript">
			<xsl:param name="is-in-header-or-footer">false</xsl:param>
      <xsl:apply-templates />
   </xsl:template>
   
   <xsl:template name="layout-master-set">
			<xsl:param name="is-in-header-or-footer">false</xsl:param>
   <fo:layout-master-set>
       <fo:simple-page-master
                         page-width="{$calculated-page-width}"
                         page-height="{$calculated-page-height}"
                         master-name="BodyPage">

       <fo:region-body   
                         margin-top="{$calculated-page-margin-top}"
                         margin-right="{$calculated-page-margin-right}"
                         margin-bottom="{$calculated-page-margin-bottom}"
                         margin-left="{$calculated-page-margin-left}" />
       <fo:region-before extent="{$calculated-page-header-extent}"
         display-align="after"/>
       <fo:region-after  extent="{$calculated-page-footer-extent}"
         precedence="true"
         display-align="before"/>
       </fo:simple-page-master>
       <fo:simple-page-master
                         page-width="{$calculated-page-width}"
                         page-height="{$calculated-page-height}"
                         master-name="CoverPage">
       <fo:region-body   margin-top="{$calculated-page-margin-top}"
                         margin-right="{$calculated-page-margin-right}"
                         margin-bottom="{$calculated-page-margin-bottom}"
                         margin-left="{$calculated-page-margin-left}" />
       </fo:simple-page-master>
   </fo:layout-master-set>
   </xsl:template>
   
   <xsl:template match="html:page-number" mode="header-or-footer">
     <fo:page-number/>
   </xsl:template>

   <xsl:template match="html:page-numbers" mode="header-or-footer">
     <fo:page-number-citation ref-id="endofdoc"/>
   </xsl:template>

   <!-- Body -->
   <xsl:template   match="html:body">
     <xsl:param name="is-in-header-or-footer">false</xsl:param>
     <fo:page-sequence master-reference="BodyPage">
       <fo:title><xsl:value-of select="/html:html/html:head/html:title" /></fo:title>
       <fo:static-content flow-name="xsl-region-before">
         <fo:block text-align="center" font-size="small">
           <fo:table width="100%">
             <fo:table-column number-columns-repeated="1" text-align="start" column-width="{$calculated-page-margin-left}"/>
             <fo:table-column number-columns-repeated="1" text-align="start" column-width="100%"/>
             <fo:table-column number-columns-repeated="1" text-align="start" column-width="{$calculated-page-margin-right}"/>
             <fo:table-body>
               <fo:table-row>
                 <fo:table-cell padding-start="0.3em" padding-end="2pt" padding-before="2pt" number-columns-spanned="1" number-rows-spanned="1">
                   <fo:block/>
                 </fo:table-cell>
                 <fo:table-cell padding-start="0.3em" padding-end="2pt" padding-before="2pt" number-columns-spanned="1" number-rows-spanned="1">
                   <fo:block>
                     <xsl:if test="string($calculated-title-print-in-header)='true'"><xsl:value-of select="/html:html/html:head/html:title" /></xsl:if>
                     <!--xsl:if test="descendant::html:page-number">              <xsl:text> - </xsl:text>              <xsl:apply-templates select="descendant::html:page-number"/>              <xsl:if test="descendant::html:page-numbers">                <xsl:text>/</xsl:text>                <xsl:apply-templates select="descendant::html:page-numbers"/>              </xsl:if>              <xsl:text> - </xsl:text>            </xsl:if -->
                     <xsl:apply-templates select="//html:page-header/*" mode="header-or-footer"/>
                   </fo:block>
                 </fo:table-cell>
                 <fo:table-cell padding-start="0.3em" padding-end="2pt" padding-before="2pt" number-columns-spanned="1" number-rows-spanned="1">
                   <fo:block/>
                 </fo:table-cell>
               </fo:table-row>
             </fo:table-body>
           </fo:table>
         </fo:block>
       </fo:static-content>
       <fo:static-content flow-name="xsl-region-after">
         <xsl:if test="$calculated-page-number-print-in-footer">
           <fo:block text-align="center" font-size="small">
             <fo:table width="100%">
               <fo:table-column number-columns-repeated="1" text-align="start" column-width="{$calculated-page-margin-left}"/>
               <fo:table-column number-columns-repeated="1" text-align="start" column-width="100%"/>
               <fo:table-column number-columns-repeated="1" text-align="start" column-width="{$calculated-page-margin-right}"/>
                 <fo:table-body>
                   <fo:table-row>
                     <fo:table-cell padding-start="0.3em" padding-end="2pt" padding-before="2pt" number-columns-spanned="1" number-rows-spanned="1">
                       <fo:block/>
                     </fo:table-cell>
                     <fo:table-cell padding-start="0.3em" padding-end="2pt" padding-before="2pt" number-columns-spanned="1" number-rows-spanned="1">
                       <fo:block>
                         <!-- xsl:if test="descendant::html:page-number">                <xsl:text> - </xsl:text>                <xsl:apply-templates select="descendant::html:page-number"/>                <xsl:if test="descendant::html:page-numbers">                  <xsl:text>/</xsl:text>                  <xsl:apply-templates select="descendant::html:page-numbers"/>                </xsl:if>                <xsl:text> - </xsl:text>              </xsl:if -->
                         <xsl:apply-templates select="//html:page-footer/*" mode="header-or-footer"/>
                       </fo:block>
                     </fo:table-cell>
                     <fo:table-cell padding-start="0.3em" padding-end="2pt" padding-before="2pt" number-columns-spanned="1" number-rows-spanned="1">
                       <fo:block/>
                     </fo:table-cell>
                   </fo:table-row>
                 </fo:table-body>
               </fo:table>
             </fo:block>
           </xsl:if>
         </fo:static-content>
         <fo:flow flow-name="xsl-region-body" >
           <xsl:apply-templates/>
           <!-- xsl:with-param name="is-in-header-or-footer">             <xsl:text>false</xsl:text>           </xsl:with-param -->
           <fo:block id="endofdoc"></fo:block>
         </fo:flow>
       </fo:page-sequence>
     </xsl:template>
 
  
     <xsl:template   match="html:div">
       <fo:block xsl:use-attribute-sets="div" >
       <xsl:choose>
         <xsl:when test="@align='Center'">
           <xsl:attribute name="display-align">
             <xsl:text>center</xsl:text>
           </xsl:attribute>
           <xsl:attribute name="text-align">
             <xsl:text>center</xsl:text>
           </xsl:attribute>
         </xsl:when>
         <xsl:when test="@align='center'">
           <xsl:attribute name="display-align">
             <xsl:text>center</xsl:text>
           </xsl:attribute>
           <xsl:attribute name="text-align">
             <xsl:text>center</xsl:text>
           </xsl:attribute>
         </xsl:when>
         <xsl:when test="@align='Right'">
           <xsl:attribute name="text-align">
             <xsl:text>right</xsl:text>
           </xsl:attribute>
         </xsl:when>
         <xsl:when test="@align='right'">
           <xsl:attribute name="text-align">
             <xsl:text>right</xsl:text>
           </xsl:attribute>
         </xsl:when>
         <xsl:when test="@align='Left'">
           <xsl:attribute name="text-align">
             <xsl:text>left</xsl:text>
           </xsl:attribute>
         </xsl:when>
         <xsl:when test="@align='left'">
           <xsl:attribute name="text-align">
             <xsl:text>left</xsl:text>
           </xsl:attribute>
         </xsl:when>
         <xsl:otherwise>
           <xsl:variable name="align" select="ancestor::*/@align[1]"/>
           <xsl:if test="$align != ''">
             <xsl:attribute name="display-align">
               <xsl:text>before</xsl:text>
             </xsl:attribute>
             <xsl:attribute name="text-align">
               <xsl:call-template name="str:to-lower">
                 <xsl:with-param name="text" select="$align"/>
               </xsl:call-template>
             </xsl:attribute>
           </xsl:if>
         </xsl:otherwise>
       </xsl:choose>
       <xsl:apply-templates/>
       </fo:block>
     </xsl:template>
     
     <xsl:template match="html:page-footer|html:page-header|html:page-footers/*|html:page-headesr/*" mode="header-or-footer">
       <fo:block xsl:use-attribute-sets="div" >
         <xsl:if test="@align='Center' | @align='center'">
           <xsl:attribute name="display-align">
             <xsl:text>center</xsl:text>
           </xsl:attribute>
           <xsl:attribute name="text-align">
             <xsl:text>center</xsl:text>
           </xsl:attribute>
         </xsl:if>
         <xsl:apply-templates mode="header-or-footer"/>
       </fo:block>
     </xsl:template>

     <xsl:template match="html:page-footer|html:page-header"/>
     <xsl:template match="html:page-footer/*|html:page-header/*"/>

   <xsl:template match="html:page-break">
			<xsl:param name="is-in-header-or-footer">false</xsl:param>
     <fo:block break-before="page">
       <xsl:apply-templates/>
     </fo:block>
   </xsl:template>
  
   <xsl:template match="html:p|html:div" mode="header-or-footer">
     <fo:block xsl:use-attribute-sets="p">
       <xsl:choose>
         <xsl:when test="@align='Center'">
           <xsl:attribute name="display-align">
             <xsl:text>center</xsl:text>
           </xsl:attribute>
           <xsl:attribute name="text-align">
             <xsl:text>center</xsl:text>
           </xsl:attribute>
         </xsl:when>
         <xsl:when test="@align='center'">
           <xsl:attribute name="display-align">
             <xsl:text>center</xsl:text>
           </xsl:attribute>
           <xsl:attribute name="text-align">
             <xsl:text>center</xsl:text>
           </xsl:attribute>
         </xsl:when>
         <xsl:when test="@align='Right'">
           <xsl:attribute name="text-align">
             <xsl:text>right</xsl:text>
           </xsl:attribute>
         </xsl:when>
         <xsl:when test="@align='right'">
           <xsl:attribute name="text-align">
             <xsl:text>right</xsl:text>
           </xsl:attribute>
         </xsl:when>
         <xsl:when test="@align='Left'">
           <xsl:attribute name="text-align">
             <xsl:text>left</xsl:text>
           </xsl:attribute>
         </xsl:when>
         <xsl:when test="@align='left'">
           <xsl:attribute name="text-align">
             <xsl:text>left</xsl:text>
           </xsl:attribute>
         </xsl:when>
         <xsl:otherwise>
           <xsl:variable name="align" select="parent::*/@align"/>
           <xsl:if test="$align != ''">
             <xsl:attribute name="display-align">
               <xsl:text>before</xsl:text>
             </xsl:attribute>
             <xsl:attribute name="text-align">
               <xsl:call-template name="str:to-lower">
                 <xsl:with-param name="text" select="$align"/>
               </xsl:call-template>
             </xsl:attribute>
           </xsl:if>
         </xsl:otherwise>
       </xsl:choose>
       <!--�     <xsl:if test="@align='Center'">�       <xsl:attribute name="display-align">�         <xsl:text>center</xsl:text>�       </xsl:attribute>�       <xsl:attribute name="text-align">�         <xsl:text>center</xsl:text>�       </xsl:attribute>�        </xsl:if>�-->
       <!--xsl:attribute name="text-align">
          <xsl:value-of select="ancestor::*[@align][1]/@align"/>
        </xsl:attribute-->
        <xsl:attribute name="font-size">
          <xsl:choose>
            <xsl:when test="font/@size">
              <xsl:value-of select="font/@size"/>
            </xsl:when>
            <xsl:otherwise>
              <!-- xsl:message>           J'ai trouv� �a comme taille de fonte:           <xsl:value-of select="ancestor::html:font[1]/@size"/>         </xsl:message -->
              <xsl:value-of select="ancestor::html:font[1]/@size"/>
            </xsl:otherwise>
          </xsl:choose>
        </xsl:attribute>
        <xsl:apply-templates mode="header-or-footer"/>
      </fo:block>
    </xsl:template>

   <xsl:template match="html:p">
     <fo:block xsl:use-attribute-sets="p">
       <xsl:choose>
         <xsl:when test="@align='Center'">
           <xsl:attribute name="display-align">
             <xsl:text>center</xsl:text>
           </xsl:attribute>
           <xsl:attribute name="text-align">
             <xsl:text>center</xsl:text>
           </xsl:attribute>
         </xsl:when>
         <xsl:when test="@align='center'">
           <xsl:attribute name="display-align">
             <xsl:text>center</xsl:text>
           </xsl:attribute>
           <xsl:attribute name="text-align">
             <xsl:text>center</xsl:text>
           </xsl:attribute>
         </xsl:when>
         <xsl:when test="@align='Right'">
           <xsl:attribute name="text-align">
             <xsl:text>right</xsl:text>
           </xsl:attribute>
         </xsl:when>
         <xsl:when test="@align='right'">
           <xsl:attribute name="text-align">
             <xsl:text>right</xsl:text>
           </xsl:attribute>
         </xsl:when>
         <xsl:when test="@align='Left'">
           <xsl:attribute name="text-align">
             <xsl:text>left</xsl:text>
           </xsl:attribute>
         </xsl:when>
         <xsl:when test="@align='left'">
           <xsl:attribute name="text-align">
             <xsl:text>left</xsl:text>
           </xsl:attribute>
         </xsl:when>
         <xsl:otherwise>
           <xsl:variable name="align" select="parent::*/@align"/>
           <xsl:if test="$align != ''">
             <xsl:attribute name="display-align">
               <xsl:text>before</xsl:text>
             </xsl:attribute>
             <xsl:attribute name="text-align">
               <xsl:call-template name="str:to-lower">
                 <xsl:with-param name="text" select="$align"/>
               </xsl:call-template>
             </xsl:attribute>
           </xsl:if>
         </xsl:otherwise>
       </xsl:choose>
       <xsl:apply-templates/>
     </fo:block>
   </xsl:template>

  
   <xsl:template match="html:h1" >
			<xsl:param name="is-in-header-or-footer">false</xsl:param>
   <fo:block  xsl:use-attribute-sets="h1">
      <xsl:apply-templates />
   </fo:block>
   </xsl:template>

  
   <xsl:template   match="html:h2" >
			<xsl:param name="is-in-header-or-footer">false</xsl:param>
   <fo:block xsl:use-attribute-sets="h2">
      <xsl:apply-templates />
   </fo:block>
   </xsl:template>

  
   <xsl:template   match="html:h3"  >
			<xsl:param name="is-in-header-or-footer">false</xsl:param>
   <fo:block xsl:use-attribute-sets="h3">
      <xsl:apply-templates />
   </fo:block>
   </xsl:template>

  
   <xsl:template   match="html:h4" >
			<xsl:param name="is-in-header-or-footer">false</xsl:param>
   <fo:block xsl:use-attribute-sets="h4">
      <xsl:apply-templates />
   </fo:block>
   </xsl:template>

   <xsl:template   match="html:h5"  >
			<xsl:param name="is-in-header-or-footer">false</xsl:param>
   <fo:block xsl:use-attribute-sets="h5">
      <xsl:apply-templates />
   </fo:block>
   </xsl:template>

  
   <xsl:template   match="html:h6" >
			<xsl:param name="is-in-header-or-footer">false</xsl:param>
   <fo:block xsl:use-attribute-sets="h6">
      <xsl:apply-templates />
   </fo:block>
   </xsl:template>

  
   <xsl:template   match="html:ul">
			<xsl:param name="is-in-header-or-footer">false</xsl:param>
   <fo:list-block  xsl:use-attribute-sets="ul">
       <xsl:apply-templates/>
   </fo:list-block>
   </xsl:template>

   <xsl:template match="html:ul/html:li">
     <fo:list-item>
       <fo:list-item-label xsl:use-attribute-sets="ul-li" end-indent="label-end()">
         <fo:block>
           <fo:character character="&#x2022;"/>
         </fo:block>
       </fo:list-item-label>
       <fo:list-item-body start-indent="body-start()">
         <fo:block>
           <xsl:apply-templates/>
         </fo:block>
       </fo:list-item-body>
     </fo:list-item>
   </xsl:template>
  
   <xsl:template   match="html:ol">
			<xsl:param name="is-in-header-or-footer">false</xsl:param>
   <fo:list-block  xsl:use-attribute-sets="ol">
       <xsl:apply-templates />
   </fo:list-block>
   </xsl:template>

   <xsl:template   match="html:ol/html:li">
     <fo:list-item>
       <fo:list-item-label xsl:use-attribute-sets="ol-li" end-indent="label-end()">
         <fo:block>
           <xsl:number format="1."/>
         </fo:block>
       </fo:list-item-label>
       <fo:list-item-body start-indent="body-start()">
         <fo:block>
           <xsl:apply-templates/>
         </fo:block>
       </fo:list-item-body>
     </fo:list-item>
   </xsl:template>

  
   <xsl:template   match="html:dl">
			<xsl:param name="is-in-header-or-footer">false</xsl:param>
      <fo:block xsl:use-attribute-sets="dl">
          <xsl:apply-templates />
      </fo:block>
   </xsl:template>

  
   <xsl:template   match="html:dt">
			<xsl:param name="is-in-header-or-footer">false</xsl:param>
      <fo:block xsl:use-attribute-sets="dt">
          <xsl:apply-templates />
      </fo:block>
   </xsl:template>

  
   <xsl:template   match="html:dd">
			<xsl:param name="is-in-header-or-footer">false</xsl:param>
      <fo:block xsl:use-attribute-sets="dd">
          <xsl:apply-templates />
      </fo:block>
   </xsl:template>

  
    <xsl:template   match="html:address">
			<xsl:param name="is-in-header-or-footer">false</xsl:param>
        <fo:block xsl:use-attribute-sets="address">
            <xsl:apply-templates />
        </fo:block>
    </xsl:template>

  
   <xsl:template   match="html:hr">
			<xsl:param name="is-in-header-or-footer">false</xsl:param>
      <fo:block space-before="1em" space-after="1em">
          <fo:leader leader-pattern="rule" rule-style="groove" leader-length="100%"/>
      </fo:block>
   </xsl:template>

  
   <xsl:template   match="html:pre">
      <fo:block xsl:use-attribute-sets="pre">
          <xsl:apply-templates mode="pre"/>
      </fo:block>
   </xsl:template>

  
   <xsl:template   match="html:blockquote">
			<xsl:param name="is-in-header-or-footer">false</xsl:param>
      <fo:block xsl:use-attribute-sets="blockquote">
          <xsl:apply-templates />
      </fo:block>
   </xsl:template>

  
   <xsl:template   match="html:ins">
			<xsl:param name="is-in-header-or-footer">false</xsl:param>
        <xsl:comment>Inserted By ins tag </xsl:comment>
        <xsl:apply-templates />
   </xsl:template>

  
   <xsl:template   match="html:del">
			<xsl:param name="is-in-header-or-footer">false</xsl:param>
   <xsl:comment> Deleted By del tag </xsl:comment>
   </xsl:template>

  
   <xsl:template   match="html:a[@href]">
			<xsl:param name="is-in-header-or-footer">false</xsl:param>
      <fo:inline xsl:use-attribute-sets="a" ><xsl:apply-templates /></fo:inline>
   </xsl:template>

  
   <xsl:template   match="html:span">
			<xsl:param name="is-in-header-or-footer">false</xsl:param>
      <xsl:apply-templates />
   </xsl:template>

  
   <xsl:template   match="html:bdo">
			<xsl:param name="is-in-header-or-footer">false</xsl:param>
      <xsl:apply-templates />
   </xsl:template>

  
   <xsl:template match="html:br">
     <!--DBG:ProcessBR-->
       <xsl:variable name="prevNode" select="generate-id(preceding-sibling::node()[1])"/>
       <xsl:variable name="prevBR" select="generate-id(preceding-sibling::html:br[1])"/>
       <xsl:variable name="prevText" select="generate-id(preceding-sibling::text()[1])"/>
       <xsl:if test='($prevNode = $prevBR) and not($prevNode = "") '>
         <!--DBG:twoBRFound/-->
         <fo:block>
           <fo:inline white-space-collapse="false">
             <xsl:text>&#x000a;</xsl:text>
           </fo:inline>
         </fo:block>
       </xsl:if>
       <xsl:if test='($prevNode = $prevText) and not($prevNode = "") '>
         <!--DBG:textBetweenBRFound/-->
         <fo:block/>
       </xsl:if>
     <!--/DBG:ProcessBR-->
   </xsl:template>

  
   <xsl:template   match="html:em">
			<xsl:param name="is-in-header-or-footer">false</xsl:param>
      <fo:inline xsl:use-attribute-sets="em">
      <xsl:apply-templates />
      </fo:inline>
   </xsl:template>

  
   <xsl:template   match="html:strong">
			<xsl:param name="is-in-header-or-footer">false</xsl:param>
      <fo:inline xsl:use-attribute-sets="strong">
      <xsl:apply-templates />
      </fo:inline>
   </xsl:template>

  
   <xsl:template   match="html:dfn">
			<xsl:param name="is-in-header-or-footer">false</xsl:param>
     <fo:inline xsl:use-attribute-sets="dfn"><xsl:apply-templates /></fo:inline>
   </xsl:template>

  
   <xsl:template   match="html:code">
			<xsl:param name="is-in-header-or-footer">false</xsl:param>
     <fo:inline xsl:use-attribute-sets="code">
     <xsl:apply-templates />
     </fo:inline>
   </xsl:template>

  
   <xsl:template   match="html:samp">
			<xsl:param name="is-in-header-or-footer">false</xsl:param>
       <fo:inline xsl:use-attribute-sets="samp">
       <xsl:apply-templates />
       </fo:inline>
   </xsl:template>

  
   <xsl:template   match="html:kbd">
			<xsl:param name="is-in-header-or-footer">false</xsl:param>
       <fo:inline xsl:use-attribute-sets="kbd">
       <xsl:apply-templates />
       </fo:inline>
   </xsl:template>

  
   <xsl:template   match="html:var">
			<xsl:param name="is-in-header-or-footer">false</xsl:param>
       <fo:inline xsl:use-attribute-sets="var">
       <xsl:apply-templates />
       </fo:inline>
   </xsl:template>

  
   <xsl:template   match="html:cite">
			<xsl:param name="is-in-header-or-footer">false</xsl:param>
      <fo:inline xsl:use-attribute-sets="cite">
       <xsl:apply-templates />
       </fo:inline>
   </xsl:template>

  
   <xsl:template   match="html:abbr">
			<xsl:param name="is-in-header-or-footer">false</xsl:param>
       <fo:inline xsl:use-attribute-sets="abbr">
       <xsl:apply-templates />
       </fo:inline>
   </xsl:template>

  
   <xsl:template   match="html:acronym">
			<xsl:param name="is-in-header-or-footer">false</xsl:param>
       <fo:inline xsl:use-attribute-sets="acronym">
       <xsl:apply-templates />
       </fo:inline>
   </xsl:template>

  
   <xsl:template   match="html:q">
			<xsl:param name="is-in-header-or-footer">false</xsl:param>
       <xsl:comment>q -- Not Support</xsl:comment>
       <xsl:apply-templates />
   </xsl:template>

  
   <xsl:template   match="html:sub">
			<xsl:param name="is-in-header-or-footer">false</xsl:param>
       <fo:inline xsl:use-attribute-sets="sub">
       <xsl:apply-templates />
       </fo:inline>
   </xsl:template>

  
   <xsl:template   match="html:sup">
			<xsl:param name="is-in-header-or-footer">false</xsl:param>
       <fo:inline xsl:use-attribute-sets="sup">
       <xsl:apply-templates />
       </fo:inline>
   </xsl:template>

  
   <xsl:template   match="html:tt">
			<xsl:param name="is-in-header-or-footer">false</xsl:param>
      <fo:inline font-family="monospace"><xsl:apply-templates /></fo:inline>
   </xsl:template>

  
   <xsl:template   match="html:i">
			<xsl:param name="is-in-header-or-footer">false</xsl:param>
      <fo:inline font-style="italic"><xsl:apply-templates /></fo:inline>
   </xsl:template>

  
   <xsl:template   match="html:b">
     <fo:inline font-weight="bold"><xsl:apply-templates /></fo:inline>
   </xsl:template>
   
   <xsl:template   match="html:big">
			<xsl:param name="is-in-header-or-footer">false</xsl:param>
         <fo:inline font-size="larger"><xsl:apply-templates /></fo:inline>
   </xsl:template>

  
   <xsl:template   match="html:small">
			<xsl:param name="is-in-header-or-footer">false</xsl:param>
        <fo:inline font-size="smaller"><xsl:apply-templates /></fo:inline>
   </xsl:template>

   <xsl:template match="html:font">
     <fo:block>
       <xsl:if test="@size">
         <!--xsl:message>
              <xsl:text>Font Size:</xsl:text>
              <xsl:value-of select="@size"/>
         </xsl:message-->
         <xsl:attribute name="font-size">
           <xsl:value-of select="@size"/>
           <xsl:text>pt</xsl:text>
         </xsl:attribute>
       </xsl:if>
       <xsl:if test="@face">
         <xsl:attribute name="font-family">
           <xsl:value-of select="@face"/>
         </xsl:attribute>
       </xsl:if>
       <xsl:apply-templates/>
     </fo:block>
     <!-- xsl:message>       <xsl:text>Found azimuth=" font, and " it'select=" " content is:</xsl:text>       <xsl:copy-of select="./*"/>     </xsl:message -->
   </xsl:template>

   <xsl:template match="html:font" mode="header-or-footer">
     <fo:block>
       <xsl:if test="@size">
         <!--xsl:message>
              <xsl:text>Font Size:</xsl:text>
              <xsl:value-of select="@size"/>
         </xsl:message-->
         <xsl:attribute name="font-size">
           <xsl:value-of select="@size"/>
           <xsl:text>pt</xsl:text>
         </xsl:attribute>
       </xsl:if>
       <xsl:if test="@face">
         <xsl:attribute name="font-family">
           <xsl:value-of select="@face"/>
         </xsl:attribute>
       </xsl:if>
       <xsl:apply-templates mode="header-or-footer"/>
     </fo:block>
     <!-- xsl:message>       <xsl:text>Found azimuth=" font, and " it'select=" " content is:</xsl:text>       <xsl:copy-of select="./*"/>     </xsl:message -->
   </xsl:template>
  
   <xsl:template   match="html:object">
			<xsl:param name="is-in-header-or-footer">false</xsl:param>
      <xsl:apply-templates />
   </xsl:template>

  
   <xsl:template match="html:param">
			<xsl:param name="is-in-header-or-footer">false</xsl:param>
      <xsl:apply-templates />
   </xsl:template>

  
   <xsl:template   match="html:html/html:body/html:img">
			<xsl:param name="is-in-header-or-footer">false</xsl:param>
   <fo:block>
   <fo:external-graphic src="{@src}">
   <xsl:if test="@height">
   <xsl:attribute name="content-height">
     <xsl:value-of select="@height" /><xsl:text>px</xsl:text>
   </xsl:attribute>
   </xsl:if>
   <xsl:choose>
     <xsl:when test="@width='*'"/>
     <xsl:otherwise>
       <xsl:if test="@width">
         <xsl:attribute name="content-width">
           <xsl:value-of select="@width"/>
           <xsl:text>px</xsl:text>
         </xsl:attribute>
         <xsl:attribute name="scaling">
           <xsl:text>uniform</xsl:text>
         </xsl:attribute>
         <xsl:attribute name="scaling-method">
           <xsl:text>auto</xsl:text>
         </xsl:attribute>
       </xsl:if>
     </xsl:otherwise>
   </xsl:choose>
   </fo:external-graphic>
   </fo:block>
   </xsl:template>
   
   <xsl:template   match="html:img">
			<xsl:param name="is-in-header-or-footer">false</xsl:param>
     <fo:external-graphic src="{@src}">
     <!-- xsl:if test="parent::[@align='Center']">       <xsl:attribute name="display-align">         <xsl:text>center</xsl:text>       </xsl:attribute>     </xsl:if -->
   <xsl:if test="@height">
   <xsl:attribute name="content-height">
     <xsl:value-of select="@height" /><xsl:text>px</xsl:text>
   </xsl:attribute>
   </xsl:if>
   <xsl:choose>
     <xsl:when test="@width='*'"/>
     <xsl:otherwise>
       <xsl:if test="@width">
         <xsl:attribute name="content-width">
           <xsl:value-of select="@width"/>
           <xsl:text>px</xsl:text>
         </xsl:attribute>
         <xsl:attribute name="scaling">
           <xsl:text>uniform</xsl:text>
         </xsl:attribute>
         <xsl:attribute name="scaling-method">
           <xsl:text>auto</xsl:text>
         </xsl:attribute>
       </xsl:if>
     </xsl:otherwise>
   </xsl:choose>
   <!--xsl:if test="@width">     <xsl:attribute name="content-width">       <xsl:choose>         <xsl:when test="@width='*'">           <xsl:text>100%</xsl:text>          </xsl:when>         <xsl:otherwise>           <xsl:value-of select="@width"/>           <xsl:text>px</xsl:text>         </xsl:otherwise>       </xsl:choose>     </xsl:attribute>   </xsl:if>     <xsl:attribute name="scaling">       <xsl:text>uniform</xsl:text>     </xsl:attribute>     <xsl:attribute name="scaling-method">       <xsl:text>auto</xsl:text>     </xsl:attribute-->
   </fo:external-graphic>
 </xsl:template>

  
   <xsl:template   match="html:map">
			<xsl:param name="is-in-header-or-footer">false</xsl:param>
   </xsl:template>

  
   <xsl:template   match="html:area">
			<xsl:param name="is-in-header-or-footer">false</xsl:param>
   </xsl:template>

  
   <xsl:template   match="html:form">
			<xsl:param name="is-in-header-or-footer">false</xsl:param>
   </xsl:template>

  
   <xsl:template   match="html:label">
			<xsl:param name="is-in-header-or-footer">false</xsl:param>
   </xsl:template>

  
   <xsl:template   match="html:input">
			<xsl:param name="is-in-header-or-footer">false</xsl:param>
   </xsl:template>

  
   <xsl:template   match="html:select">
			<xsl:param name="is-in-header-or-footer">false</xsl:param>
   </xsl:template>

  
   <xsl:template   match="html:optgroup">
			<xsl:param name="is-in-header-or-footer">false</xsl:param>
   </xsl:template>

  
   <xsl:template   match="html:option">
			<xsl:param name="is-in-header-or-footer">false</xsl:param>
   </xsl:template>

  
   <xsl:template   match="html:textarea">
			<xsl:param name="is-in-header-or-footer">false</xsl:param>
   </xsl:template>

  
   <xsl:template   match="html:fieldset">
			<xsl:param name="is-in-header-or-footer">false</xsl:param>
    </xsl:template>

  
   <xsl:template   match="html:legend">
			<xsl:param name="is-in-header-or-footer">false</xsl:param>
   </xsl:template>

  
   <xsl:template   match="html:button">
			<xsl:param name="is-in-header-or-footer">false</xsl:param>
   </xsl:template>

  
  <xsl:template match="html:table">
			<xsl:param name="is-in-header-or-footer">false</xsl:param>
    <!-- xsl:comment>      <fo:table-and-caption>        <xsl:if test="html:caption">          <fo:table-caption xsl:use-attribute-sets="table-caption">            <xsl:apply-templates select="html:caption"/>          </fo:table-caption>        </xsl:if>      </fo:table-and-caption>    </xsl:comment -->
   <fo:table xsl:use-attribute-sets="table">
     <xsl:if test="@width">
       <xsl:attribute name="width">
         <xsl:value-of select="@width"/>
         <xsl:if test="not(contains(@width, '%')) and not(contains(@width, '*'))">
           <xsl:text>px</xsl:text>
         </xsl:if>
       </xsl:attribute>
     </xsl:if>
     <xsl:if test="@border and not (@border = '0')">
       <!--xsl:message>         <xsl:text>BORDER:</xsl:text>         <xsl:value-of select="@border"/>       </xsl:message-->
       <xsl:attribute name="border-style">solid</xsl:attribute>
       <xsl:attribute name="border-width">
         <xsl:value-of select="@border"/>
       </xsl:attribute>
     </xsl:if>
     
      <xsl:if test="@bgcolor">
        <xsl:attribute name="background-color">
          <xsl:value-of select="@bgcolor"/>
        </xsl:attribute>
      </xsl:if>
     <xsl:if test="not(html:col | html:colgroup)">
       <xsl:message>
         <xsl:text><![CDATA[Found no <COLGROUP SPAN="X"/> in your HTML file!!! We'll try to guess your number of columns!]]></xsl:text>
       </xsl:message>
       <xsl:variable name="span">
         <xsl:choose>
           <xsl:when test="count(html:thead/html:tr[1]/html:td) > 0">
             <xsl:value-of select="count(html:thead/html:tr[1]/html:td)"/>
             <xsl:message>
               <xsl:text>[xy]Guessed </xsl:text>
               <xsl:value-of select="count(html:thead/html:tr[1]/html:td)"/>
               <xsl:text> column(s) for </xsl:text>
               <xsl:value-of select="normalize-space(html:thead/html:tr[1])"/>
             </xsl:message>
           </xsl:when>
           <xsl:when test="count(html:tbody/html:tr[1]/html:td) > 0">
             <xsl:value-of select="count(html:tbody/html:tr[1]/html:td)"/>
             <xsl:message>
               <xsl:text>[0]Guessed </xsl:text>
               <xsl:value-of select="count(html:tbody/html:tr[1]/html:td)"/>
               <xsl:text> column(s) for </xsl:text>
               <xsl:value-of select="normalize-space(html:tbody/html:tr[1])"/>
             </xsl:message>
           </xsl:when>
           <xsl:when test="count(html:thead/html:tr[1]/html:td) > 0">
             <xsl:value-of select="count(html:thead/html:tr[1]/html:td)"/>
             <xsl:message>
               <xsl:text>[x]Guessed </xsl:text>
               <xsl:value-of select="count(html:thead/html:tr[1]/html:td)"/>
               <xsl:text> column(s) for </xsl:text>
               <xsl:value-of select="normalize-space(html:thead/html:tr[1])"/>
             </xsl:message>
           </xsl:when>
           <xsl:when test="count(html:tr[1]/html:td) > 0">
             <xsl:value-of select="count(html:tr[1]/html:td)"/>
             <xsl:message>
               <xsl:text>[1]Guessed </xsl:text>
               <xsl:value-of select="count(html:tr[1]/html:td)"/>
               <xsl:text> column(s) for </xsl:text>
               <xsl:value-of select="normalize-space(html:tr[1])"/>
             </xsl:message>
           </xsl:when>
           <!-- <TD COLSPAN=2> -->
           <xsl:when test="count(html:tr[1]/html:td[1]/@colspan) > 0">
             <xsl:value-of select="html:tr[1]/html:td[1]/@colspan"/>
             <xsl:message>
               <xsl:text>[2]Guessed </xsl:text>
               <xsl:value-of select="html:tr[1]/html:td[1]/@colspan"/>
               <xsl:text> column(s) for</xsl:text>
               <xsl:value-of select="normalize-space(html:tr[1])"/>
             </xsl:message>
           </xsl:when>
           <xsl:otherwise>
             <xsl:value-of select="count(html:tr[1]/html:td)"/>
             <xsl:message>
               <xsl:text>[2]Guessed </xsl:text>
               <xsl:value-of select="count(html:tr[1]/html:td)"/>
               <xsl:text> column(s) for</xsl:text>
               <xsl:value-of select="normalize-space(html:tr[1])"/>
             </xsl:message>
           </xsl:otherwise>
         </xsl:choose>
       </xsl:variable>
       <xsl:call-template name="make-column">
         <xsl:with-param name="span" select="$span" />
         <!-- xsl:with-param name="width">           <xsl:value-of select="100 div $span"/>           <xsl:text>%</xsl:text>         </xsl:with-param -->
         </xsl:call-template>
       </xsl:if>
       <xsl:apply-templates select="html:col | html:colgroup"/>
       <xsl:apply-templates select="html:thead"/>
       <xsl:apply-templates select="html:tfoot"/>
       <xsl:choose>
       <xsl:when test="html:tbody">
           <xsl:apply-templates select="html:tbody"/>
       </xsl:when>
       <xsl:otherwise>
           <fo:table-body>
                <xsl:apply-templates select="html:tr"/>
           </fo:table-body>
       </xsl:otherwise>
       </xsl:choose>
   </fo:table>
   <!-- /fo:table-and-caption -->
   </xsl:template>

  
   <xsl:template   match="html:caption">
			<xsl:param name="is-in-header-or-footer">false</xsl:param>
      <fo:block><xsl:apply-templates /></fo:block>
   </xsl:template>

  
   <xsl:template   match="html:thead">
			<xsl:param name="is-in-header-or-footer">false</xsl:param>
   <fo:table-header>
      <xsl:apply-templates select="html:tr"/>
   </fo:table-header>
   </xsl:template>

  
   <xsl:template   match="html:tfoot">
			<xsl:param name="is-in-header-or-footer">false</xsl:param>
   <fo:table-footer>
      <xsl:apply-templates select="html:tr"/>
   </fo:table-footer>
   </xsl:template>

  
   <xsl:template   match="html:tbody">
     <fo:table-body>
       <!--xsl:apply-templates select="html:tr"/-->
       <xsl:apply-templates/>
     </fo:table-body>
   </xsl:template>

   <xsl:template   match="html:colgroup">
			<xsl:param name="is-in-header-or-footer">false</xsl:param>
     <xsl:choose>
      <xsl:when test="html:col">
        <xsl:apply-templates select="html:col"/>
      </xsl:when>
      <xsl:otherwise>    <xsl:variable name="width">
    <xsl:choose>
      <xsl:when test="@width"><xsl:value-of select="@width" /></xsl:when>
      <xsl:otherwise></xsl:otherwise>
    </xsl:choose>
    </xsl:variable>
    <xsl:variable name="align">
    <xsl:choose>
      <xsl:when test="@align"><xsl:value-of select="@align" /></xsl:when>
      <xsl:otherwise>relative</xsl:otherwise><!-- inherit -->
    </xsl:choose>
    </xsl:variable>
    <xsl:variable name="span">
    <xsl:choose>
      <xsl:when test="@span"><xsl:value-of select="@span" /></xsl:when>
      <xsl:otherwise>1</xsl:otherwise>
    </xsl:choose>
    </xsl:variable>
    <xsl:call-template name="make-column">
    <xsl:with-param name="width" select="$width" />
    <xsl:with-param name="align" select="$align" />
    <xsl:with-param name="span" select="$span" />
    </xsl:call-template>

      </xsl:otherwise>
     </xsl:choose>
   </xsl:template>
   
   
   <xsl:template   match="html:colgroup/html:col">
			<xsl:param name="is-in-header-or-footer">false</xsl:param>
    <xsl:variable name="width">
    <xsl:choose>
      <xsl:when test="@width"><xsl:value-of select="@width" /></xsl:when>
      <xsl:otherwise>
        <xsl:if test="../@width" >
        <xsl:value-of select="../@width" />
        </xsl:if>
      </xsl:otherwise>
    </xsl:choose>
    </xsl:variable>
    <xsl:variable name="align">
    <xsl:choose>
      <xsl:when test="@align"><xsl:value-of select="@align" /></xsl:when>
        <xsl:otherwise>
          <xsl:choose>
            <xsl:when test="../@align" >
              <xsl:value-of select="../@align" />
            </xsl:when>
            <xsl:otherwise>1</xsl:otherwise>
          </xsl:choose>
       </xsl:otherwise>
    </xsl:choose>
    </xsl:variable>
    <xsl:variable name="span">
    <xsl:choose>
      <xsl:when test="@span"><xsl:value-of select="@span" /></xsl:when>
      <xsl:otherwise>1</xsl:otherwise>
    </xsl:choose>
    </xsl:variable>
    <xsl:call-template name="make-column">
    <xsl:with-param name="width" select="$width" />
    <xsl:with-param name="align" select="$align" />
    <xsl:with-param name="span" select="$span" />
    </xsl:call-template>

   </xsl:template>
   
  <xsl:template match="html:table/html:col">
			<xsl:param name="is-in-header-or-footer">false</xsl:param>
    <xsl:variable name="width">
    <xsl:choose>
      <xsl:when test="@width">
        <xsl:choose>
          <xsl:when test="substring(@width,string-length(@width))='%'">
            <xsl:text>proportional-column-width(</xsl:text>
            <xsl:value-of select="substring(@width,1,string-length(@width) - 1)"/>
            <xsl:text>)</xsl:text>
          </xsl:when>
          <xsl:otherwise>
            <xsl:value-of select="@width"/>
          </xsl:otherwise>
        </xsl:choose>
      </xsl:when>
    </xsl:choose>
    </xsl:variable>
    <xsl:variable name="align">
    <xsl:choose>
      <xsl:when test="@align"><xsl:value-of select="@align" /></xsl:when>
      <xsl:otherwise>inherit</xsl:otherwise>
    </xsl:choose>
    </xsl:variable>
    <xsl:variable name="span">
    <xsl:choose>
      <xsl:when test="@span"><xsl:value-of select="@span" /></xsl:when>
      <xsl:otherwise>1</xsl:otherwise>
    </xsl:choose>
    </xsl:variable>
    <xsl:call-template name="make-column">
    <xsl:with-param name="width" select="$width" />
    <xsl:with-param name="align" select="$align" />
    <xsl:with-param name="span" select="$span" />
    </xsl:call-template>
  </xsl:template>
  
  <xsl:template name="make-column">
    <xsl:param name="width"/>
    <xsl:param name="align"/>
    <xsl:param name="span"/>
    <xsl:choose>
      <xsl:when test="html:thead/html:tr[1]/html:td[@width]">
        <xsl:for-each select="html:thead/html:tr[1]/html:td">
          <fo:table-column>
            <xsl:choose>
              <xsl:when test="@width">
                <xsl:attribute name = "column-width">
                  <xsl:text>proportional-column-width(</xsl:text>
                  <xsl:choose>
                    <xsl:when test="substring(@width,string-length(@width))='%'">
                      <xsl:value-of select="substring(@width,1,string-length(@width) - 1)"/>
                    </xsl:when>
                    <xsl:otherwise>
                      <xsl:value-of select="@width"/>
                    </xsl:otherwise>
                  </xsl:choose>
                  <xsl:text>)</xsl:text>
                </xsl:attribute>
              </xsl:when>
              <xsl:otherwise>
              </xsl:otherwise>
            </xsl:choose>
            <xsl:attribute name = "number-columns-repeated">
              <!--xsl:value-of select="$span"/-->
              <xsl:text>1</xsl:text>
            </xsl:attribute>
            <xsl:attribute name="text-align">
              <xsl:choose>
                <xsl:when test="$align = 'Left'">left</xsl:when>
                <xsl:when test="$align = 'left'">left</xsl:when>
                <xsl:when test="$align = 'Center'">center</xsl:when>
                <xsl:when test="$align = 'center'">center</xsl:when>
                <xsl:when test="$align = 'Right'">right</xsl:when>
                <xsl:when test="$align = 'right'">right</xsl:when>
                <xsl:otherwise>start</xsl:otherwise>
              </xsl:choose>
            </xsl:attribute>
          </fo:table-column>
        </xsl:for-each>
      </xsl:when>
      <xsl:when test="html:tr[1]/html:td[@width]">
        <xsl:for-each select="html:tr[1]/html:td"> <!-- [@width] -->
          <fo:table-column>
            <xsl:choose>
              <xsl:when test="@width">
                <xsl:attribute name = "column-width">
                  <xsl:text>proportional-column-width(</xsl:text>
                  <xsl:choose>
                    <xsl:when test="substring(@width,string-length(@width))='%'">
                      <xsl:value-of select="substring(@width,1,string-length(@width) - 1)"/>
                    </xsl:when>
                    <xsl:otherwise>
                      <xsl:value-of select="@width"/>
                    </xsl:otherwise>
                  </xsl:choose>
                  <xsl:text>)</xsl:text>
                </xsl:attribute>
              </xsl:when>
              <xsl:otherwise>
              </xsl:otherwise>
            </xsl:choose>
            <xsl:attribute name = "number-columns-repeated">
              <!--xsl:value-of select="$span"/-->
              <xsl:text>1</xsl:text>
            </xsl:attribute>
            <xsl:attribute name="text-align">
              <xsl:choose>
                <xsl:when test="$align = 'Left'">left</xsl:when>
                <xsl:when test="$align = 'left'">left</xsl:when>
                <xsl:when test="$align = 'Center'">center</xsl:when>
                <xsl:when test="$align = 'center'">center</xsl:when>
                <xsl:when test="$align = 'Right'">right</xsl:when>
                <xsl:when test="$align = 'right'">right</xsl:when>
                <xsl:otherwise>start</xsl:otherwise>
              </xsl:choose>
            </xsl:attribute>
          </fo:table-column>
        </xsl:for-each>
      </xsl:when>
      <xsl:otherwise>
        <fo:table-column>
          <xsl:choose>
            <xsl:when test="not(@width) and ($width)">
              <xsl:attribute name = "column-width">
                <xsl:value-of select="$width"/>
              </xsl:attribute>
            </xsl:when>
            <xsl:otherwise>
            </xsl:otherwise>
          </xsl:choose>
          <xsl:attribute name = "number-columns-repeated">
            <xsl:value-of select="$span"/>
          </xsl:attribute>
          <xsl:attribute name="text-align">
            <xsl:choose>
              <xsl:when test="$align = 'Left'">left</xsl:when>
              <xsl:when test="$align = 'left'">left</xsl:when>
              <xsl:when test="$align = 'Center'">center</xsl:when>
              <xsl:when test="$align = 'center'">center</xsl:when>
              <xsl:when test="$align = 'Right'">right</xsl:when>
              <xsl:when test="$align = 'right'">right</xsl:when>
              <xsl:otherwise>start</xsl:otherwise>
            </xsl:choose>
          </xsl:attribute>
        </fo:table-column>
      </xsl:otherwise>
    </xsl:choose>
  </xsl:template>

  <xsl:template match="html:tr">
    <xsl:param name="is-in-header-or-footer">false</xsl:param>
    <fo:table-row xsl:use-attribute-sets="tr">
      <xsl:if test="@bgcolor">
        <xsl:attribute name="background-color">
          <xsl:value-of select="@bgcolor"/>
        </xsl:attribute>
      </xsl:if>
      <xsl:apply-templates select="html:th | html:td"/>
    </fo:table-row>
  </xsl:template>

  <xsl:template match="html:th">
			<xsl:param name="is-in-header-or-footer">false</xsl:param>
     <fo:table-cell xsl:use-attribute-sets="th">
        <xsl:call-template name="make-cell" />
     </fo:table-cell>
  </xsl:template>

  <xsl:template match="html:td">
    <fo:table-cell xsl:use-attribute-sets="td">
      <xsl:if test="@bgcolor">
        <xsl:attribute name="background-color">
          <xsl:value-of select="@bgcolor"/>
        </xsl:attribute>
      </xsl:if>
      <xsl:if test="@align='left'">
        <xsl:attribute name="padding-start">
          <xsl:text>0</xsl:text>
        </xsl:attribute>
      </xsl:if>
        <xsl:call-template name="make-cell" />
    </fo:table-cell>
  </xsl:template>
 
 
 <xsl:template name="make-cell">

    <!--<xsl:attribute name="text-align">from-table-column(text-align)</xsl:attribute>-->
    <xsl:if test="@border and not (@border = '0')">
      <!--xsl:message>         <xsl:text>BORDER:</xsl:text>         <xsl:value-of select="@border"/>       </xsl:message-->
      <xsl:attribute name="border-style">solid</xsl:attribute>
      <xsl:attribute name="border-width">
        <xsl:value-of select="@border"/>
      </xsl:attribute>
    </xsl:if>
   <xsl:if test="@colspan">
      <xsl:attribute name="number-columns-spanned">
        <xsl:value-of select="@colspan"/>
      </xsl:attribute>
   </xsl:if>
   <xsl:if test="@rowspan">
      <xsl:attribute name="number-rows-spanned">
        <xsl:value-of select="@rowspan"/>
      </xsl:attribute>
   </xsl:if>
   <xsl:if test="@align">
      <xsl:attribute name="text-align">
        <xsl:choose>
        <xsl:when test="@align = 'Left'">left</xsl:when>
        <xsl:when test="@align = 'left'">left</xsl:when>
        <xsl:when test="@align = 'Center'">center</xsl:when>
        <xsl:when test="@align = 'center'">center</xsl:when>
        <xsl:when test="@align = 'Right'">right</xsl:when>
        <xsl:when test="@align = 'right'">right</xsl:when>
        <xsl:otherwise>center</xsl:otherwise> <!-- inherit -->
        </xsl:choose>
      </xsl:attribute>
   </xsl:if>
   <fo:block>
     <xsl:if test="ancestor::html:font[1]">
       <xsl:attribute name="font-size">
         <!-- xsl:message>           J'ai trouv� �a comme taille de fonte:           <xsl:value-of select="ancestor::html:font[1]/@size"/>         </xsl:message -->
         <xsl:value-of select="ancestor::html:font[1]/@size"/>
       </xsl:attribute>
     </xsl:if>
   <xsl:apply-templates />
   </fo:block>
  </xsl:template>

  <xsl:template match="texti">
			<xsl:param name="is-in-header-or-footer">false</xsl:param>
    <!--xsl:text>coucou</xsl:text>    <xsl:call-template name="str:subst">      <xsl:with-param name="text" select="."/>      <xsl:with-param name="replace">        <xsl:text>&amp;nbsp;</xsl:text>      </xsl:with-param>      <xsl:with-param name="with">        <xsl:text>&#x160;</xsl:text>      </xsl:with-param>    </xsl:call-template-->
    <fo:inline>
      <xsl:value-of select="."/>
    </fo:inline>
  </xsl:template>
 
  <xsl:template match="comment()">
			<xsl:param name="is-in-header-or-footer">false</xsl:param>
    <xsl:comment>
      <xsl:value-of select="."/>
    </xsl:comment>
  </xsl:template>

  <!-- xsl:template match="html:sp">     <xsl:message>       On coupe!     </xsl:message>     <fo:block break-before="page"> woupue     </fo:block>   </xsl:template -->
  <xsl:template match="html:table" mode="header-or-footer">
			<xsl:param name="is-in-header-or-footer">false</xsl:param>
    <!-- xsl:comment>      <fo:table-and-caption>        <xsl:if test="html:caption">          <fo:table-caption xsl:use-attribute-sets="table-caption">            <xsl:apply-templates select="html:caption"/>          </fo:table-caption>        </xsl:if>      </fo:table-and-caption>    </xsl:comment -->
   <fo:table xsl:use-attribute-sets="table">
     <xsl:if test="@width">
       <xsl:attribute name="width">
         <xsl:value-of select="@width"/>
         <xsl:if test="not(contains(@width, '%')) and not(contains(@width, '*'))">
           <xsl:text>px</xsl:text>
         </xsl:if>
       </xsl:attribute>
     </xsl:if>
     <xsl:if test="@border">
       <xsl:attribute name="border-style"><xsl:value-of select="@border" /></xsl:attribute>
     </xsl:if>
     
      <xsl:if test="@bgcolor">
        <xsl:attribute name="background-color">
          <xsl:value-of select="@bgcolor"/>
        </xsl:attribute>
      </xsl:if>
     <xsl:if test="not(html:col | html:colgroup)">
       <xsl:message>
         <xsl:text><![CDATA[Found no <COLGROUP SPAN="X"/> in your HTML file!!! We'll try to guess your number of columns!]]></xsl:text>
       </xsl:message>
       <xsl:variable name="span">
         <xsl:choose>
           <xsl:when test="count(html:tbody/html:tr[1]/html:td) > 0">
             <xsl:value-of select="count(html:tbody/html:tr[1]/html:td)"/>
             <xsl:message>
               <xsl:text>[0]Guessed </xsl:text>
               <xsl:value-of select="count(html:tbody/html:tr[1]/html:td)"/>
               <xsl:text> column(s) for </xsl:text>
               <xsl:value-of select="normalize-space(html:tbody/html:tr[1])"/>
             </xsl:message>
           </xsl:when>
           <xsl:when test="count(html:thead/html:tr[1]/html:td) > 0">
             <xsl:value-of select="count(html:thead/html:tr[1]/html:td)"/>
             <xsl:message>
               <xsl:text>[1][Thead]Guessed </xsl:text>
               <xsl:value-of select="count(html:thead/html:tr[1]/html:td)"/>
               <xsl:text> column(s) for </xsl:text>
               <xsl:value-of select="normalize-space(html:thead/html:tr[1])"/>
             </xsl:message>
           </xsl:when>
           <xsl:when test="count(html:tr[1]/html:td) > 0">
             <xsl:value-of select="count(html:tr[1]/html:td)"/>
             <xsl:message>
               <xsl:text>[2]Guessed </xsl:text>
               <xsl:value-of select="count(html:tr[1]/html:td)"/>
               <xsl:text> column(s) for </xsl:text>
               <xsl:value-of select="normalize-space(html:tr[1])"/>
             </xsl:message>
           </xsl:when>
           <!-- <TD COLSPAN=2> -->
           <xsl:when test="count(html:tr[1]/html:td[1]/@colspan) > 0">
             <xsl:value-of select="html:tr[1]/html:td[1]/@colspan"/>
             <xsl:message>
               <xsl:text>[3]Guessed </xsl:text>
               <xsl:value-of select="html:tr[1]/html:td[1]/@colspan"/>
               <xsl:text> column(s) for</xsl:text>
               <xsl:value-of select="normalize-space(html:tr[1])"/>
             </xsl:message>
           </xsl:when>
           <xsl:otherwise>
             <xsl:value-of select="count(html:tr[1]/html:td)"/>
             <xsl:message>
               <xsl:text>[4]Guessed </xsl:text>
               <xsl:value-of select="count(html:tr[1]/html:td)"/>
               <xsl:text> column(s) for</xsl:text>
               <xsl:value-of select="normalize-space(html:tr[1])"/>
             </xsl:message>
           </xsl:otherwise>
         </xsl:choose>
       </xsl:variable>
       <xsl:call-template name="make-column">
         <xsl:with-param name="span" select="$span" />
         <!-- xsl:with-param name="width">           <xsl:value-of select="100 div $span"/>           <xsl:text>%</xsl:text>         </xsl:with-param -->
         </xsl:call-template>
       </xsl:if>
       <xsl:apply-templates select="html:col | html:colgroup"/>
       <xsl:apply-templates select="html:thead"/>
       <xsl:apply-templates select="html:tfoot"/>
       <xsl:choose>
       <xsl:when test="html:tbody">
           <xsl:apply-templates select="html:tbody"/>
       </xsl:when>
       <xsl:otherwise>
           <fo:table-body>
                <xsl:apply-templates select="html:tr"/>
           </fo:table-body>
       </xsl:otherwise>
       </xsl:choose>
   </fo:table>
   <!-- /fo:table-and-caption -->
   </xsl:template>

   <xsl:template match="xsl:*/@*">
     <xsl:copy-of select="."/>
   </xsl:template>

   <xsl:template match="xsl:select">
     <xsl:attribute name="xsl:select">
       <xsl:value-of select="."/>
     </xsl:attribute>
   </xsl:template>

   <xsl:template match="xsl:*">
     <xsl:copy>
       <xsl:apply-templates select="@*|*"/>
     </xsl:copy>
   </xsl:template>
</xsl:stylesheet>