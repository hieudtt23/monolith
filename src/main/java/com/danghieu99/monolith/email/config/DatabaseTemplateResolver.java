package com.danghieu99.monolith.email.config;

import com.danghieu99.monolith.email.entity.EmailTemplate;
import com.danghieu99.monolith.email.repository.EmailTemplateRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.thymeleaf.IEngineConfiguration;
import org.thymeleaf.templateresolver.StringTemplateResolver;
import org.thymeleaf.templateresource.ITemplateResource;

import java.util.Map;
import java.util.Optional;

@Component
@Slf4j
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class DatabaseTemplateResolver extends StringTemplateResolver {

    private EmailTemplateRepository emailTemplateRepository;

    @Override
    protected ITemplateResource computeTemplateResource(IEngineConfiguration configuration,
                                                        String ownerTemplate,
                                                        String templateName,
                                                        Map<String, Object> templateResolutionAttributes) {
        log.info("Loading template with name: {} from database", templateName);
        Optional<EmailTemplate> emailTemplate = emailTemplateRepository.findByName(templateName);
        if (emailTemplate.isEmpty()) {
            return null;
        }
        return super.computeTemplateResource(configuration, ownerTemplate, emailTemplate.get().getContent(), templateResolutionAttributes);
    }
}
