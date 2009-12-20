<!--

    Copyright 2009 Robin Komiwes, Bruno Verachten, Christophe Cordenier

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

    	http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.

-->

<xsl:stylesheet
xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
xmlns:xhtml="http://www.w3.org/1999/xhtml"
version="1.0">

<!--
$Id: xh2latex.xsl,v 1.14 2006/10/27 01:34:15 connolly Exp $

cribbed heavily from
XSLT from XHTML+MathML to LATEX
Eitan M. Gurari July 19, 2000
http://www.cse.ohio-state.edu/~gurari/docs/mml-00/xhm2latex.html#x1-20001

see also

Beginner's LaTeX Tutorial
http://www.csclub.uwaterloo.ca/u/sjbmann/tutorial.html
-->

<xsl:output method="text"/>

<xsl:param name="Bib" select='"bibdata"'/>
<xsl:param name="Status" select='"prepub"'/>


<xsl:template match="xhtml:html">

  <!-- @@ docStyle is lightly tested; undocumented -->
  <xsl:variable name="docClass"
	       select='xhtml:head/xhtml:link[@rel="documentclass"]/@title' />
  <xsl:if test="$docClass">
    <xsl:text> \documentclass{</xsl:text>
    <xsl:value-of select='$docClass' />
    <xsl:text>}&#10;</xsl:text>
  </xsl:if>

  <xsl:variable name="docStyle"
	       select='xhtml:head/xhtml:link[@rel="documentstyle"]/@title' />
  <xsl:if test="$docStyle">
    <xsl:text> \documentstyle{</xsl:text>
    <xsl:value-of select='$docStyle' />
    <xsl:text>}&#10;</xsl:text>
  </xsl:if>

  <xsl:for-each select='xhtml:head/xhtml:link[@rel="usepackage"]'>
    <xsl:text>  \usepackage{</xsl:text>
    <xsl:value-of select="@title" />
    <xsl:text>}&#10;</xsl:text>
  </xsl:for-each>

  <!--
  \DeclareGraphicsRule{.gif}{eps}{}{}
  -->

  <xsl:text>
  \begin{document}
  </xsl:text>

  <xsl:variable name="bibStyle"
		select='xhtml:head/xhtml:link[@rel="bibliographystyle"]/@title' />
  <xsl:if test="$bibStyle">
    <xsl:text> \bibliographystyle{</xsl:text>
    <xsl:value-of select='$bibStyle' />
    <xsl:text>}&#10;</xsl:text>
  </xsl:if>

  <xsl:apply-templates select="xhtml:body"/>

  <xsl:text>\end{document}
  </xsl:text>
</xsl:template>


<!-- suppress online-only material -->
<xsl:template match="*[contains(@class, 'online')]">
  <!-- noop -->
</xsl:template>

<xsl:template match="*[contains(@class, 'online')]" mode="maketitle">
  <!-- noop -->
</xsl:template>


<!-- frontmatter: title page -->
<xsl:template match="xhtml:div[@class='maketitle']">
  <xsl:apply-templates mode="maketitle"/>
  <!-- what on earth does this \label thing do? -->
  <xsl:text>\maketitle
\label{firstpage}
  </xsl:text>
</xsl:template>

<xsl:template match="xhtml:div[@class='date']" mode="maketitle">
</xsl:template>

<xsl:template match="xhtml:h1" mode="maketitle">
  <xsl:text>\title{</xsl:text>
  <xsl:apply-templates/>
  <xsl:text>}&#10;</xsl:text>
</xsl:template>

<xsl:template match="*[@class='subtitle']" mode="maketitle">
  <xsl:text>\subtitle{</xsl:text>
  <xsl:apply-templates/>
  <xsl:text>}&#10;</xsl:text>
</xsl:template>

<!-- WWW2006 style author markup, ala
 http://www.acm.org/sigs/pubs/proceed/sigfaq.htm#a17 -->
<xsl:template match='xhtml:table[contains(@class, "author")]' mode="maketitle">
  <xsl:text>\numberofauthors{</xsl:text>
  <xsl:value-of select='count(xhtml:tr/xhtml:td)' />
  <xsl:text>}&#10;</xsl:text>

  <xsl:text>\author{&#10;</xsl:text>

  <xsl:for-each select="xhtml:tr/xhtml:td">
    <xsl:text>\alignauthor </xsl:text>

    <xsl:apply-templates mode="maketitle"/>

    <xsl:text>&#10;</xsl:text>
  </xsl:for-each>
  <xsl:text>}&#10;</xsl:text>
</xsl:template>

<xsl:template match='*[@class="affaddr"]' mode="maketitle">
  <xsl:text>\\&#10; \affaddr{</xsl:text>
  <xsl:value-of select="normalize-space(.)" />
  <xsl:text>}</xsl:text>
</xsl:template>

<xsl:template match='*[@class="email"]' mode="maketitle">
  <xsl:text>\email{</xsl:text>
  <xsl:apply-templates />
  <xsl:text>}</xsl:text>
</xsl:template>

<xsl:template match='xhtml:br' mode="maketitle">
  <xsl:text> \\&#10;</xsl:text>
</xsl:template>

<xsl:template match='xhtml:a' mode="maketitle">
  <xsl:apply-templates />
</xsl:template>

<xsl:template match='text()' mode="maketitle">
  <xsl:apply-templates />
</xsl:template>

<xsl:template match="xhtml:address" mode="maketitle">
  <xsl:text>\author</xsl:text>

  <!-- should add a test for this -->
  <xsl:if test='.//xhtml:a/@title'>
    <xsl:text>[</xsl:text>
    <xsl:value-of select='.//xhtml:a/@title' />
    <xsl:text>]</xsl:text>
  </xsl:if>    
  <xsl:text>{</xsl:text>
  <xsl:for-each select='xhtml:a[contains(@rel, "author")]'>
    <xsl:value-of select='.'/>
    <xsl:if test='following-sibling::xhtml:a[contains(@rel, "author")]'>
      <xsl:text> \and </xsl:text>
    </xsl:if>
  </xsl:for-each>
  
  <xsl:for-each select='*[contains(@class, "vcard")]'>
    <xsl:apply-templates mode="author" />
    <xsl:if test='following-sibling::*[contains(@class, "vcard")]'>
      <xsl:text> \and </xsl:text>
    </xsl:if>
  </xsl:for-each>
  
  <xsl:for-each select='*[@class="thanks"]'>
    <xsl:text>\thanks{</xsl:text>
    <xsl:apply-templates/>
    <xsl:text>}</xsl:text>
  </xsl:for-each>
  
  <xsl:text>}
  </xsl:text>
  
  <xsl:if test='xhtml:a[@rel="institute"]'>
    <xsl:text>\institute{</xsl:text>
    <xsl:for-each select='xhtml:a[@rel="institute"]'>
      <xsl:value-of select='.'/>
      <xsl:if test='following-sibling::xhtml:a[@rel="institute"]'>
	<xsl:text> \and </xsl:text>
      </xsl:if>
    </xsl:for-each>
    <xsl:text>}
    </xsl:text>
  </xsl:if>
</xsl:template>

<xsl:template match="xhtml:br" mode="author">
  <xsl:text>\\&#10;</xsl:text>
</xsl:template>
<xsl:template match="text()" mode="author">
  <xsl:value-of select="normalize-space(.)" />
</xsl:template>


<!-- abstract -->
<xsl:template match="xhtml:div[@class='abstract']">
  <xsl:text>
    \begin{abstract}
  </xsl:text>
  <xsl:apply-templates/>

  <xsl:variable name="Address" select='normalize-space(//xhtml:address)'/>

  <xsl:variable name="RCSDate">
    <xsl:value-of
     select ='normalize-space(substring-before(substring-after(
     $Address,
     concat("$", "Date:")), "$"))'/>
  </xsl:variable>

  <xsl:variable name="Revision">
    <xsl:value-of
     select ='substring-before(substring-after(
     $Address,
     concat("$", "Revision:")), "$")'/>
  </xsl:variable>

  <xsl:if test='$Status="prepub" and $RCSDate'>

    <!-- convert yyyy/mm/dd hh:mm:ss to yyyy-mm-ddThh:mm:ssZ -->
    <xsl:variable name="ISODate">
	<xsl:value-of
	 select =' concat(translate($RCSDate, "/ ", "-T"), "Z")'/>
    </xsl:variable>

    <xsl:text>\par{\bf PRE-PUBLICATION DRAFT </xsl:text>
    <xsl:value-of select="$Revision"/>
    <xsl:text> of </xsl:text>
    <xsl:value-of select="$ISODate"/>
    <xsl:text>. DO NOT CIRCULATE.}</xsl:text>
  </xsl:if>

  <xsl:text>\end{abstract}
  </xsl:text>
</xsl:template>

<!-- suppress redundant heading -->
<xsl:template match="xhtml:h4[normalize-space()='Abstract']">
  <!-- noop -->
</xsl:template>
<xsl:template match="xhtml:h3[normalize-space()='Abstract']">
  <!-- noop -->
</xsl:template>
<xsl:template match="xhtml:h2[normalize-space()='Abstract']">
  <!-- noop -->
</xsl:template>

<xsl:template match='xhtml:div[@class="terms"]'>
  <xsl:text>\terms{</xsl:text>
  <xsl:apply-templates/>
  <xsl:text>}
  </xsl:text>
</xsl:template>
<xsl:template match='xhtml:div[@class="keywords"]'>
  <xsl:text>\begin{keywords}</xsl:text>
  <xsl:apply-templates/>
  <xsl:text>\end{keywords}
  </xsl:text>
</xsl:template>

<!-- body sections -->
<xsl:template match="xhtml:h2">
  <xsl:text>\section{</xsl:text>
  <xsl:apply-templates/>
  <xsl:text>}
  </xsl:text>
  <xsl:call-template name="section-label" />
</xsl:template>


<xsl:template match="xhtml:h3">
  <xsl:text>\subsection{</xsl:text>
  <xsl:apply-templates/>
  <xsl:text>}
  </xsl:text>
  <xsl:call-template name="section-label" />
</xsl:template>

<xsl:template match="xhtml:h4">
  <xsl:text>\subsubsection{</xsl:text>
  <xsl:apply-templates/>
  <xsl:text>}
  </xsl:text>
  <xsl:call-template name="section-label" />
</xsl:template>

<!-- section labels. -->
<xsl:template name="section-label">
  <xsl:variable name="label" select="../@id" />
  <!--xsl:message>
    section-label: <xsl:value-of select="$label"/>
    <xsl:value-of select="position()"/>
    <xsl:value-of select="."/>
  </xsl:message-->

  <xsl:if test="$label">
    <xsl:text>\label{</xsl:text>
    <xsl:value-of select="$label" />
    <xsl:text>}&#10;</xsl:text>
  </xsl:if>
</xsl:template>


<!-- lists -->
<xsl:template match="xhtml:ul">
  <xsl:text>\begin{itemize}
  </xsl:text>
  <xsl:for-each select="xhtml:li">
    <xsl:text>\item </xsl:text>
    <xsl:apply-templates />
  </xsl:for-each>
  <xsl:text>
    \end{itemize}
  </xsl:text>
</xsl:template>

<xsl:template match="xhtml:ol">
  <xsl:text>\begin{enumerate}
  </xsl:text>
  <xsl:for-each select="xhtml:li">
    <xsl:text>\item </xsl:text>
    <xsl:apply-templates />
  </xsl:for-each>
  <xsl:text>
    \end{enumerate}
  </xsl:text>
</xsl:template>

<xsl:template match="xhtml:dl">
  <xsl:text>\begin{description}
  </xsl:text>
  <xsl:for-each select="*">
    <xsl:if test='local-name() = "dt"'>
      <xsl:text>\item[</xsl:text>
    </xsl:if>
    <xsl:apply-templates />

    <xsl:if test='local-name() = "dt"'>
      <xsl:text>] </xsl:text>
    </xsl:if>
  </xsl:for-each>
  <xsl:text>
    \end{description}
  </xsl:text>
</xsl:template>

<!-- tables -->
<xsl:template match="xhtml:table[@border='1']">
  <xsl:text>\begin{center}</xsl:text>
  <xsl:text>\begin{tabular}{|</xsl:text>
  <xsl:for-each select="xhtml:tr[1]/*">
    <xsl:text>c|</xsl:text>
  </xsl:for-each>
  <xsl:text>}&#10;</xsl:text>

  <xsl:for-each select="xhtml:tr">
    <xsl:text>\hline&#10;</xsl:text>
    <xsl:for-each select="*">
      <xsl:if test="name() = 'th'">{\bf </xsl:if>
      <xsl:apply-templates />
      <xsl:if test="name() = 'th'">}</xsl:if>
      <xsl:if test="position() != last()">
	<xsl:text> &amp; </xsl:text>
      </xsl:if>
    </xsl:for-each>
    <xsl:text> \\&#10;</xsl:text>
  </xsl:for-each>
  <xsl:text>\hline&#10;</xsl:text>

  <xsl:text>\end{tabular}&#10;</xsl:text>
  <xsl:text>\end{center}&#10;</xsl:text>
</xsl:template>

<!-- ol, img code untested -->
<xsl:template match="xhtml:ol[@class='enumerate1']">
  <xsl:text>\begin{enumerate}</xsl:text>
  <xsl:for-each select="xhtml:li">
    <xsl:text>\item </xsl:text>
    <xsl:apply-templates />
  </xsl:for-each>
  <xsl:text>\end{enumerate}</xsl:text>
</xsl:template>

<xsl:template match="xhtml:img[@class='graphics'
	      or @class='includegraphics']">
  <xsl:text>\includegraphics[width=</xsl:text>
  <xsl:value-of select="@width"/>
  <xsl:text>,height=</xsl:text>
  <xsl:value-of select="@height"/>
  <xsl:text>]{</xsl:text>
  <xsl:value-of select="@src"/>
  <xsl:text>}</xsl:text>
</xsl:template>


<!-- blockquote -->
<xsl:template match="xhtml:blockquote">
  <xsl:text>
    \begin{quote}
  </xsl:text>
  <xsl:apply-templates />
  <xsl:text>
    \end{quote}
  </xsl:text>
</xsl:template>

<!-- equasion -->
<xsl:template match="xhtml:p[@class='eqn-display']">
  <xsl:text>
    \begin{equation}
  </xsl:text>
  <xsl:apply-templates mode="math"/>
  <xsl:text>
    \end{equation}
  </xsl:text>
</xsl:template>

<!-- example, definition -->
<xsl:template match='xhtml:pre[@class="definition"]'>
  <xsl:text>\begin{</xsl:text>
  <xsl:value-of select='@class'/>
  <xsl:text>}</xsl:text>
  <xsl:text>\begin{verbatim}</xsl:text>
  <xsl:text>&#10;</xsl:text>
  <xsl:text>&#10;</xsl:text>
  <xsl:apply-templates mode="verbatim"/>
  <xsl:text>\end{verbatim}</xsl:text>
  <xsl:text>\end{</xsl:text>
  <xsl:value-of select='@class'/>
  <xsl:text>}</xsl:text>
  <xsl:text>&#10;</xsl:text>
</xsl:template>

<!-- misc pre/verbatim -->
<xsl:template match="xhtml:pre">
  <xsl:text>\begin{verbatim}</xsl:text>
  <xsl:apply-templates mode="verbatim"/>
  <xsl:text>\end{verbatim}</xsl:text>
</xsl:template>


<!-- paragraphs -->

<xsl:template match="xhtml:p">
  <xsl:text>&#10;</xsl:text>
  <xsl:text>\par </xsl:text>
  <xsl:apply-templates/>
</xsl:template>

<xsl:template match="xhtml:p[@class='nopar']">
  <xsl:text>\empty </xsl:text>
  <xsl:apply-templates/>
  <xsl:text>\empty </xsl:text>
</xsl:template>

<xsl:template match='xhtml:p[@class="proposition" or @class="definition"]'>
  <xsl:text>\begin{</xsl:text>
  <xsl:value-of select='@class'/>
  <xsl:text>}</xsl:text>
  <xsl:text>&#10;</xsl:text>
  <xsl:apply-templates/>
  <xsl:text>\end{</xsl:text>
  <xsl:value-of select='@class'/>
  <xsl:text>}</xsl:text>
  <xsl:text>&#10;</xsl:text>
</xsl:template>


<!-- footnotes -->
<xsl:template match='*[@class="footnote"]'>
  <xsl:text>\footnote{</xsl:text>
  <xsl:apply-templates/>
  <xsl:text>}</xsl:text>
</xsl:template>

<!-- phrase markup -->

<xsl:template match="xhtml:em|xhtml:dfn">
  <xsl:text>{\em </xsl:text>
  <xsl:apply-templates/>
  <xsl:text>}</xsl:text>
</xsl:template>

<xsl:template match="xhtml:code">
  <xsl:text>{\tt </xsl:text>
  <xsl:apply-templates/>
  <xsl:text>}</xsl:text>
</xsl:template>

<xsl:template match="xhtml:tt">
  <xsl:text>{\tt </xsl:text>
  <xsl:apply-templates/>
  <xsl:text>}</xsl:text>
</xsl:template>

<xsl:template match='*[@class="url"]'>
  <xsl:text>\url{ </xsl:text>
  <xsl:apply-templates/>
  <xsl:text>}</xsl:text>
</xsl:template>

<xsl:template match="xhtml:i">
  <xsl:text>{\it </xsl:text>
  <xsl:apply-templates/>
  <xsl:text>}</xsl:text>
</xsl:template>

<xsl:template match="xhtml:b">
  <xsl:text>{\bf </xsl:text>
  <xsl:apply-templates/>
  <xsl:text>}</xsl:text>
</xsl:template>

<xsl:template match="xhtml:q">
  <xsl:text>``</xsl:text>
  <xsl:apply-templates />
  <xsl:text>''</xsl:text>
</xsl:template>

<xsl:template match="xhtml:samp">
  <!-- pass-thru, for \Sigma -->
  <xsl:text>$</xsl:text>
  <xsl:value-of select='.'/>
  <xsl:text>$</xsl:text>
</xsl:template>

<xsl:template match="xhtml:samp" mode="math">
  <!-- pass-thru, for \Sigma -->
  <xsl:value-of select='.'/>
</xsl:template>


<!-- citation links -->
<xsl:template match='xhtml:a[starts-with(., "[")]'>
  <xsl:text>\cite{</xsl:text>
  <xsl:value-of select="substring-after(@href,'#')"/>
  <xsl:text>}</xsl:text>
</xsl:template>

<!-- section/figure references -->
<xsl:template match='xhtml:a[@rel="ref"]'>
  <xsl:text>\ref{</xsl:text>
  <xsl:value-of select="substring-after(@href,'#')"/>
  <xsl:text>}</xsl:text>
</xsl:template>

<!-- wierd phrase markup. @@not tested -->
<xsl:template match="xhtml:span[@class='cmtt-10']">
  <xsl:text>{\tt </xsl:text>
  <xsl:apply-templates />
  <xsl:text>}</xsl:text>
</xsl:template>
<xsl:template match="xhtml:span[@class='cmti-10']">
  <xsl:text>{\it </xsl:text>
  <xsl:apply-templates />
  <xsl:text>}</xsl:text>
</xsl:template>


<!-- text -->
<xsl:template match="text()">
  <!-- 
  per latex tutorial, the following need escaping: # $ % & ~ _ ^ \ { }
  -->

  <xsl:call-template name="esc">
    <xsl:with-param name="c" select='"#"'/>
    <xsl:with-param name="s">
      <xsl:call-template name="esc">
	<xsl:with-param name="c" select='"$"'/>
	<xsl:with-param name="s">
	  <xsl:call-template name="esc">
	    <xsl:with-param name="c" select='"%"'/>
	    <xsl:with-param name="s">
	      <xsl:call-template name="esc">
		<xsl:with-param name="c" select='"&amp;"'/>
		<xsl:with-param name="s">
		  <xsl:call-template name="esc">
		    <xsl:with-param name="c" select='"~"'/>
		    <xsl:with-param name="s">
		      <xsl:call-template name="esc">
			<xsl:with-param name="c" select='"_"'/>
			<xsl:with-param name="s">
			  <xsl:call-template name="esc">
			    <xsl:with-param name="c" select='"^"'/>
			    <xsl:with-param name="s">
			      <xsl:call-template name="esc">
				<xsl:with-param name="c" select='"{"'/>
				<xsl:with-param name="s">
				  <xsl:call-template name="esc">
				    <xsl:with-param name="c" select='"}"'/>
				    <xsl:with-param name="s">
				      <xsl:call-template name="esc">
					<xsl:with-param name="c" select='"\"'/>
					<xsl:with-param name="s" select='.'/>
				      </xsl:call-template>
				    </xsl:with-param>
				  </xsl:call-template>
				</xsl:with-param>
			      </xsl:call-template>
			    </xsl:with-param>
			  </xsl:call-template>
			</xsl:with-param>
		      </xsl:call-template>
		    </xsl:with-param>
		  </xsl:call-template>
		</xsl:with-param>
	      </xsl:call-template>
	    </xsl:with-param>
	  </xsl:call-template>
	</xsl:with-param>
      </xsl:call-template>
    </xsl:with-param>
  </xsl:call-template>
</xsl:template>

<xsl:template name="esc">
  <xsl:param name="s"/>
  <xsl:param name="c"/>

  <xsl:choose>
    <xsl:when test='contains($s, $c)'>
      <xsl:value-of select='substring-before($s, $c)'/>
      <xsl:text>\</xsl:text>

      <xsl:choose>
	<xsl:when test='$c = "\"'>
	  <xsl:text>textbackslash </xsl:text>
	</xsl:when>
	<xsl:otherwise>
	  <xsl:value-of select='$c'/>
	</xsl:otherwise>
      </xsl:choose>

      <xsl:call-template name="esc">
	<xsl:with-param name='c' select='$c'/>
	<xsl:with-param name='s' select='substring-after($s, $c)'/>
      </xsl:call-template>
    </xsl:when>
    <xsl:otherwise>
      <xsl:value-of select='$s'/>
    </xsl:otherwise>
  </xsl:choose>
</xsl:template>


<!-- bibliography -->
<xsl:template match="xhtml:h2[normalize-space()='References']">
  <!-- noop. suppress redundant heading -->
</xsl:template>

<xsl:template match="xhtml:dl[@class='bib']">
  <xsl:text>\bibliography{</xsl:text>
  <xsl:value-of select='$Bib'/>
  <xsl:text>}</xsl:text>

  <!-- the content of this dl is handled by xh2bib.xsl -->
</xsl:template>
<xsl:template match="xhtml:ul[@class='bibliography']">
  <xsl:text>\bibliography{</xsl:text>
  <xsl:value-of select='$Bib'/>
  <xsl:text>}</xsl:text>

  <!-- the content of this dl is handled by xh2bib.xsl -->
</xsl:template>


<xsl:template match='xhtml:div[@class="figure"]'>
  <!-- dunno what [tb] is for, exactly @@-->
  <xsl:text>\begin{figure}[tb]&#10;</xsl:text>

  <xsl:for-each select="xhtml:object">
    <xsl:text>\centerline{\epsfig{file=</xsl:text>
    <xsl:value-of select="@data" />
    <xsl:text>, height=</xsl:text>
    <xsl:value-of select="@height" />
    <xsl:text>}}&#10;</xsl:text>
  </xsl:for-each>

  <xsl:apply-templates />
  <xsl:text>\end{figure}&#10;</xsl:text>
</xsl:template>

<xsl:template match='xhtml:p[@class="caption"]'>
  <xsl:text>\caption{</xsl:text>
  <xsl:apply-templates />
  <xsl:text>}</xsl:text>
  <xsl:call-template name="section-label" />
</xsl:template>


<xsl:template match="xhtml:td[@class='figure']">
  <xsl:text>\begin{figure}</xsl:text>
  <xsl:apply-templates/>
  <xsl:text>\end{figure}</xsl:text>
</xsl:template>

<xsl:template match="xhtml:div[@class='caption']">
  <xsl:apply-templates select="xhtml:table" mode="caption" />
</xsl:template>

<xsl:template match="xhtml:table" mode="caption">
  <xsl:apply-templates select="xhtml:tr" mode="caption" />
</xsl:template>

<xsl:template match="xhtml:tr" mode="caption">
  <xsl:apply-templates select="xhtml:td[2]" mode="caption" />
</xsl:template>

<xsl:template match="xhtml:td" mode="caption">
  <xsl:text>\caption{</xsl:text>
  <xsl:apply-templates />
  <xsl:text>}</xsl:text>
</xsl:template>


<!-- @@hmm... links? untested -->
<xsl:template match="xhtml:a" >
  <xsl:choose>
    <xsl:when test="descendant::comment()[starts-with(.,'tex4ht:ref:')]">
      <xsl:text>\ref{</xsl:text>
      <xsl:value-of select="substring-after(@href,'#')"/>
      <xsl:text>}% </xsl:text>
      <xsl:apply-templates mode="ref"
			   select="descendant::comment()[starts-with(.,'tex4ht:ref:')]" />
      <xsl:text>
      </xsl:text>
    </xsl:when>
    <xsl:otherwise>
      <xsl:text>\empty </xsl:text>
      <xsl:apply-templates />
    </xsl:otherwise>
  </xsl:choose>
</xsl:template>

<xsl:template  mode="ref"
	       match="comment()[starts-with(.,'tex4ht:ref:')]">
  <xsl:value-of select="substring-after(.,'tex4ht:ref:')"/>
</xsl:template>

<xsl:template match="comment()[starts-with(.,'tex4ht:label?:')]">
  <xsl:text>\label{\empty </xsl:text>
  <xsl:value-of select="substring-after(.,'tex4ht:label?:')"/>
  <xsl:text>}</xsl:text>
</xsl:template>






</xsl:stylesheet>

