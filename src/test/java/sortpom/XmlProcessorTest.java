package sortpom;

import org.apache.commons.io.IOUtils;
import org.jdom.Content;
import org.jdom.Element;
import org.junit.Test;
import sortpom.util.FileUtil;
import sortpom.util.ReflectionHelper;
import sortpom.wrapper.AlphabeticalSortedWrapper;
import sortpom.wrapper.GroupWrapper;
import sortpom.wrapper.UnsortedWrapper;
import sortpom.wrapper.Wrapper;
import sortpom.wrapper.WrapperFactory;
import sortpom.wrapper.WrapperFactoryImpl;
import sortpom.wrapper.WrapperOperations;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;

import static org.junit.Assert.assertEquals;

public class XmlProcessorTest {

    private static final String UTF_8 = "UTF-8";

    @Test
    public final void testSortXmlAttributes() throws Exception {
        testInputAndExpected("src/test/resources/Attribute_input.xml", "src/test/resources/Attribute_expected.xml",
                false, false);
    }

    @Test
    public final void testSortXmlCharacter() throws Exception {
        testInputAndExpected("src/test/resources/Character_input.xml", "src/test/resources/Character_expected.xml",
                false, false);
    }

    @Test
    public final void testSortXmlComplex() throws Exception {
        testInputAndExpected("src/test/resources/Complex_input.xml", "src/test/resources/Complex_expected.xml", false, false);
    }

    @Test
    public final void testSortXmlFullFromAlfabeticalOrder() throws Exception {
        testInputAndExpected("src/test/resources/full_alfa_input.xml", "src/test/resources/full_expected.xml", false, false);
    }

    @Test
    public final void testSortXmlFullToAlfabetical() throws Exception {
        testInputAndExpected("src/test/resources/full_unsorted_input.xml", "src/test/resources/full_alfa_input.xml",
                true, false);
    }

    @Test
    public final void testSortXmlMultilineComment() throws Exception {
        testInputAndExpected("src/test/resources/MultilineComment_input.xml",
                "src/test/resources/MultilineComment_expected.xml", false, false);
    }

    @Test
    public final void testSortXmlReal1() throws Exception {
        testInputAndExpected("src/test/resources/Real1_input.xml", "src/test/resources/Real1_expected.xml", false, false);
    }

    @Test
    public final void testSortXmlSimple() throws Exception {
        testInputAndExpected("src/test/resources/Simple_input.xml", "src/test/resources/Simple_expected.xml", false, false);
    }

    public void testInputAndExpected(final String inputFileName, final String expectedFileName,
                                     final boolean sortAlfabeticalOnly, boolean keepBlankLines) throws Exception {
        PluginParameters pluginParameters = new PluginParametersBuilder().setPomFile(null).setBackupInfo(false, ".bak")
                .setFormatting("UTF-8", "\r\n", "  ", true, keepBlankLines).setSortOrder("default_0_4_0.xml", null)
                .setSortEntities(false, false, false).createPluginParameters();
        final String xml = IOUtils.toString(new FileInputStream(inputFileName), UTF_8);
        final FileUtil fileUtil = new FileUtil();
        WrapperFactory wrapperFactory = new WrapperFactoryImpl(fileUtil);
        ((WrapperFactoryImpl) wrapperFactory).setup(pluginParameters);
        final XmlProcessor xmlProcessor = new XmlProcessor(wrapperFactory);
        fileUtil.setup(pluginParameters);
        xmlProcessor.setup(pluginParameters);
        if (sortAlfabeticalOnly) {
            wrapperFactory = new WrapperFactory() {

                @Override
                public WrapperOperations createFromRootElement(final Element rootElement) {
                    return new GroupWrapper(new AlphabeticalSortedWrapper(rootElement));
                }

                @SuppressWarnings("unchecked")
                @Override
                public <T extends Content> Wrapper<T> create(final T content) {
                    if (content instanceof Element) {
                        Element element = (Element) content;
                        return (Wrapper<T>) new AlphabeticalSortedWrapper(element);
                    }
                    return new UnsortedWrapper<T>(content);
                }

                @Override
                public void initialize() {
                }

            };
        } else {
            new ReflectionHelper(wrapperFactory).setField(fileUtil);
        }
        wrapperFactory.initialize();
        new ReflectionHelper(xmlProcessor).setField(wrapperFactory);
        xmlProcessor.setOriginalXml(new ByteArrayInputStream(xml.getBytes(UTF_8)));
        xmlProcessor.sortXml();
        final ByteArrayOutputStream sortedXmlOutputStream = xmlProcessor.getSortedXml();
        final String expected = IOUtils.toString(new FileInputStream(expectedFileName), UTF_8);
        String actual = sortedXmlOutputStream.toString(UTF_8);
        assertEquals(expected, actual);
    }
}
