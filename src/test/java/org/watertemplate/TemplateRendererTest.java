package org.watertemplate;

import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class TemplateRendererTest {

    @Test
    public void templateOnlyWithMasterTemplate() {
        class TemplateOnlyWithMasterTemplate extends Template {
            protected String getTemplateFileURI() {
                return "_template_only_with_master_template.st";
            }

            protected Template getMasterTemplate() {
                return new Fixture.MasterTemplate();
            }
        }

        String rendered = render(new TemplateOnlyWithMasterTemplate());
        Assert.assertEquals("master_template_content\n" + "template_only_with_master_template_content", rendered);
    }

    @Test
    public void templateOnlyWithSubTemplates() {
        class TemplateOnlyWithSubTemplates extends Template {
            protected String getTemplateFileURI() {
                return "_template_only_with_sub_templates.st";
            }

            protected Map<String, Template> getSubTemplates() {
                HashMap<String, Template> subTemplates = new HashMap<>();
                subTemplates.put("sub_template", new Fixture.SubTemplate());
                return subTemplates;
            }
        }

        String rendered = render(new TemplateOnlyWithSubTemplates());
        Assert.assertEquals("template_only_with_sub_templates_content\n" + "sub_template_content", rendered);
    }

    @Test
    public void templateWithSubTemplatesAndMasterTemplate() {
        class TemplateWithSubTemplatesAndMasterTemplate extends Template {
            protected String getTemplateFileURI() {
                return "_template_with_sub_templates_and_master_template.st";
            }

            protected Template getMasterTemplate() {
                return new Fixture.MasterTemplate();
            }

            protected Map<String, Template> getSubTemplates() {
                HashMap<String, Template> subTemplates = new HashMap<>();
                subTemplates.put("sub_template", new Fixture.SubTemplate());
                return subTemplates;
            }
        }

        String rendered = render(new TemplateWithSubTemplatesAndMasterTemplate());
        Assert.assertEquals("master_template_content\n" +
            "template_with_sub_templates_and_master_template_content\n" +
            "sub_template_content", rendered);
    }

    @Test
    public void templateWithMasterTemplateAndSubTemplatesThatHaveAMasterTemplate() {
        class SubTemplateMasterTemplate extends Template {
            protected String getTemplateFileURI() {
                return "_sub_template_master_template.st";
            }
        }

        class SubTemplateWithMasterTemplate extends Fixture.SubTemplate {
            protected Template getMasterTemplate() {
                return new SubTemplateMasterTemplate();
            }
        }

        class TemplateWithMasterTemplateAndSubTemplatesThatHaveAMasterTemplate extends Template {
            protected String getTemplateFileURI() {
                return "_template_with_master_template_and_sub_templates_that_have_a_master_template.st";
            }

            protected Template getMasterTemplate() {
                return new Fixture.MasterTemplate();
            }

            protected Map<String, Template> getSubTemplates() {
                HashMap<String, Template> subTemplates = new HashMap<>();
                subTemplates.put("sub_template", new SubTemplateWithMasterTemplate());
                return subTemplates;
            }
        }

        String rendered = render(new TemplateWithMasterTemplateAndSubTemplatesThatHaveAMasterTemplate());
        Assert.assertEquals("" +
                "master_template_content\n" +
                "template_that_have_sub_templates_that_have_a_master_template_content\n" +
                "sub_template_master_template_content\n" +
                "sub_template_content", rendered
        );
    }

    @Test(expected = Exception.class)
    public void templateWithInvalidTemplatePath() {
        render(new Template() {
            protected String getTemplateFileURI() {
                return "_invalid.st";
            }
        });
    }

    @Test
    public void templateWithListArgs() {
        class TemplateWithListArgs extends Template {
            protected String getTemplateFileURI() {
                return "_template_with_list_args.st";
            }

            TemplateWithListArgs() {
                add("items", new int[]{1, 2});
            }
        }

        String rendered = render(new TemplateWithListArgs());
        Assert.assertEquals("1\n2\n", rendered);
    }

    @Test
    public void templateWithNonexistentLocale() {
        Assert.assertEquals("sub_template_content", render(new Fixture.SubTemplate(), Locale.FRANCE));
    }

    private String render(final Template template) {
        return render(template, TemplateRenderer.DEFAULT_LOCALE);
    }

    private String render(final Template template, final Locale locale) {
        return new TemplateRenderer(template, locale).render();
    }
}
