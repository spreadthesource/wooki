<?xml version="1.0" encoding="ISO-8859-1"?>
<xsl:stylesheet
  xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
  xmlns:xhtml="http://www.w3.org/1999/xhtml"
  version="1.0">

  <xsl:output method="xml"/>

  <xsl:template match="/"> 
    <html xmlns="http://www.w3.org/1999/xhtml">
      <head>
        <title />
        <meta content="Wooki" name="generator" />
        <meta name="description" content="If your book has an abstract then it should go here." />
      </head>
      <body>
        <div class="book">
          <div class="titlepage">
            <div>
              <div>
                <h1 class="title">
                  <a id="N10001" />An Example Book</h1>
                </div>
                <div>
                  <div class="author">
                    <h3 class="author">
                      <span class="firstname"><xsl:value-of select="/xhtml:html/xhtml:head/xhtml:meta[@name='author']/@content"/></span>
                      <span class="surname"><xsl:value-of select="/xhtml:html/xhtml:head/xhtml:meta[@name='author']/@content"/></span>
                    </h3>
                    <div class="affiliation">
                      <div class="address">
                        <p>
                          <code class="email">&lt;<a class="email" href="mailto:foo@example.com">foo@example.com</a>&gt;</code>
                        </p>
                      </div>
                    </div>
                  </div>
                </div>
                <div>
                  <p class="copyright"> © 2000 Copyright string here</p>
                </div>
                <div>
                  <div class="abstract">
                    <p class="title">
                      <b />
                    </p>
                    <p>If your book has an abstract then it should go here.</p>
                  </div>
                </div>
              </div>
              <hr />
            </div>
            <div class="toc">
              <p>
                <b />
              </p>
              <dl>
                <dt>
                  <span class="preface">
                    <a href="#N10017">Preface</a>
                  </span>
                </dt>
                <dt>
                  <span class="chapter">
                    <a href="#N1001C">1. My First Chapter</a>
                  </span>
                </dt>
                <dd>
                  <dl>
                    <dt>
                      <span class="sect1">
                        <a href="#N10021">My First Section</a>
                      </span>
                    </dt>
                  </dl>
                </dd>
              </dl>
            </div>
            <div class="preface">
              <div class="titlepage">
                <div>
                  <div>
                    <h2 class="title">
                      <a xmlns:saxon="http://icl.com/saxon" id="N10017" />
                    </h2>
                  </div>
                </div>
              </div>
              <p>Your book may have a preface, in which case it should be placed        here.</p>
            </div>
            <xsl:for-each select="/xhtml:html/xhtml:body/xhtml:div[@class='section']">
              <xsl:copy>
                <xsl:apply-templates select="@*"/>
                <xsl:attribute  name="class"><xsl:text>chapter</xsl:text></xsl:attribute>
                <xsl:apply-templates select="node()"/>
              </xsl:copy>
            </xsl:for-each>
          </div>
        </body>
      </html>
    </xsl:template>

    <xsl:template match="@*|node()">
      <xsl:copy>
        <xsl:apply-templates select="@*|node()"/>
      </xsl:copy>
    </xsl:template>

  </xsl:stylesheet>