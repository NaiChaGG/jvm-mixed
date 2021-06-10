<?xml version="1.0" encoding="utf-8"?>
<!--
  ~ Copyright 2021 JoJo Wang , homepage: https://github.com/jojoti.
  ~
  ~ Licensed under the Apache License, Version 2.0 (the "License");
  ~ you may not use this file except in compliance with the License.
  ~ You may obtain a copy of the License at
  ~
  ~ http://www.apache.org/licenses/LICENSE-2.0
  ~
  ~ Unless required by applicable law or agreed to in writing, software
  ~ distributed under the License is distributed on an "AS IS" BASIS,
  ~ WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  ~ See the License for the specific language governing permissions and
  ~ limitations under the License.
  -->

<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:m="http://maven.apache.org/POM/4.0.0" version="2.0"
                exclude-result-prefixes="m">
    <xsl:output xmlns:xslt="https://xml.apache.org/xslt" method="xml" encoding="utf-8"
                indent="yes" xslt:indent-amount="4"/>
    <xsl:strip-space elements="*"/>
    <xsl:template match="@*|node()">
        <xsl:copy>
            <xsl:apply-templates select="@*|node()"/>
        </xsl:copy>
    </xsl:template>
    <xsl:template match="/m:project/m:properties">
        <xsl:copy>
            <xsl:apply-templates select="node()">
                <xsl:sort select="name()"/>
            </xsl:apply-templates>
        </xsl:copy>
    </xsl:template>
    <xsl:template
            match="/m:project/m:dependencyManagement/m:dependencies/m:dependency/m:version/text()[. = '${revision}']">
        <xsl:value-of select="/m:project/m:version/text()"/>
    </xsl:template>
    <xsl:template
            match="/m:project/m:build/m:pluginManagement/m:plugins/m:plugin/m:version/text()[. = '${revision}']">
        <xsl:value-of select="/m:project/m:version/text()"/>
    </xsl:template>
    <xsl:template match="/m:project/m:properties/m:revision"/>
    <xsl:template match="/m:project/m:properties/m:main.basedir"/>
    <xsl:template match="/m:project/m:distributionManagement"/>
</xsl:stylesheet>