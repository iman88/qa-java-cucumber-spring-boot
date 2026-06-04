package com.qa.kelvin.service;

import com.microsoft.playwright.Page;
import jakarta.annotation.PostConstruct;
import net.datafaker.Faker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Lazy
@Service
public class ScreenshotService {

    @Autowired
    private ApplicationContext ctx;

    @Value("${screenshot.path}")
    private Path path;

    @Autowired
    private Faker faker;

    @PostConstruct
    public void init() throws IOException {
        Files.createDirectories(this.path);
    }

    public void takeScreenShot() throws IOException {
        byte[] screenshot = ctx.getBean(Page.class).screenshot();
        Files.write(this.path.resolve(faker.name().firstName() + ".png"), screenshot);
    }

    public byte[] getScreenshot() {
        return ctx.getBean(Page.class).screenshot();
    }
}
