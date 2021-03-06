// Copyright (C) 2003-2009 by Object Mentor, Inc. All rights reserved.
// Released under the terms of the CPL Common Public License version 1.0.
package fitnesse.testsystems.slim;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import fitnesse.slim.SlimError;
import fitnesse.testsystems.TestPage;
import fitnesse.testsystems.TestSystemListener;
import fitnesse.testsystems.slim.tables.SlimTable;
import fitnesse.wiki.*;
import fitnesse.wikitext.parser.ParsedPage;
import org.htmlparser.Parser;
import org.htmlparser.lexer.Lexer;
import org.htmlparser.lexer.Page;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;

public class HtmlSlimTestSystem extends SlimTestSystem {
  private HtmlTableScanner tableScanner;

  public HtmlSlimTestSystem(WikiPage page, Descriptor descriptor, TestSystemListener listener) {
    super(page, descriptor, listener);
  }

  @Override
  protected List<SlimTable> createSlimTables(TestPage pageToTest) {
    NodeList nodeList = makeNodeList(pageToTest.getDecoratedData());
    tableScanner = new HtmlTableScanner(nodeList);
    return createSlimTables(tableScanner);
  }

  private NodeList makeNodeList(ReadOnlyPageData pageData) {
    String html;
    ParsedPage parsedPage = pageData.getParsedPage();
    html = parsedPage.toHtml();
    Parser parser = new Parser(new Lexer(new Page(html)));
    try {
      return parser.parse(null);
    } catch (ParserException e) {
      throw new SlimError(e);
    }
  }

  @Override
  protected String createHtmlResults(SlimTable startWithTable, SlimTable stopBeforeTable) {
    HtmlTable start = (startWithTable != null) ? (HtmlTable) startWithTable.getTable() : null;
    HtmlTable end = (stopBeforeTable != null) ? (HtmlTable) stopBeforeTable.getTable() : null;
    return tableScanner.toHtml(start, end);
  }
}
