/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2010, Red Hat, Inc., and individual contributors
 * as indicated by the @author tags. See the copyright.txt file in the
 * distribution for a full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package org.jboss.hal.client.skeleton;

import com.gwtplatform.mvp.client.ViewImpl;
import elemental.dom.Element;
import org.jboss.gwt.elemento.core.DataElement;
import org.jboss.gwt.elemento.core.Elements;
import org.jboss.gwt.elemento.core.EventHandler;
import org.jboss.gwt.elemento.core.Templated;
import org.jboss.hal.ballroom.ProgressElement;
import org.jboss.hal.config.Environment;
import org.jboss.hal.config.semver.Version;
import org.jboss.hal.core.ui.UIRegistry;
import org.jboss.hal.resources.Resources;
import org.jboss.hal.resources.UIConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;

import static org.jboss.gwt.elemento.core.EventType.click;

/**
 * @author Harald Pehl
 */
@Templated("MainLayout.html#footer")
public abstract class FooterView extends ViewImpl implements FooterPresenter.MyView {

    // @formatter:off
    public static FooterView create(final UIRegistry uiRegistry, final Resources resources) {
        return new Templated_FooterView(uiRegistry, resources);
    }

    public abstract UIRegistry uiRegistry();
    public abstract Resources resources();
    // @formatter:on


    private static Logger logger = LoggerFactory.getLogger(FooterView.class);

    private FooterPresenter presenter;
    private Environment environment;

    @DataElement ProgressElement progress = new ProgressElement();
    @DataElement Element halVersion;
    @DataElement Element updateAvailable;

    @PostConstruct
    void init() {
        uiRegistry().register(progress);
        Elements.setVisible(updateAvailable, false);
    }

    @Override
    public void setPresenter(final FooterPresenter presenter) {
        this.presenter = presenter;
    }

    public void updateEnvironment(Environment environment) {
        this.environment = environment;
        halVersion.setInnerText(environment.getHalVersion().toString());
    }

    @Override
    public void updateVersion(final Version version) {
        if (version.greaterThan(environment.getHalVersion())) {
            logger.info("A new HAL version is available. Current version: {}, new version: {}", //NON-NLS
                    environment.getHalVersion(), version);
            String updateAvailable = resources().messages().updateAvailable(environment.getHalVersion().toString(),
                    version.toString());
            this.updateAvailable.setTitle(updateAvailable);
            this.updateAvailable.getDataset().setAt(UIConstants.TOGGLE, UIConstants.TOOLTIP);
            this.updateAvailable.getDataset().setAt(UIConstants.PLACEMENT, "top");
            this.updateAvailable.getDataset().setAt("container", "body"); //NON-NLS
            Elements.setVisible(this.updateAvailable, true);
        }
    }

    @EventHandler(element = "showVersion", on = click)
    void onShowVersion() {
        presenter.onShowVersion();
    }

    @EventHandler(element = "modelBrowser", on = click)
    void onModelBrowser() {
        presenter.onModelBrowser();
    }

    @EventHandler(element = "expressionResolver", on = click)
    void onExpressionResolver() {
        presenter.onExpressionResolver();
    }

    @EventHandler(element = "settings", on = click)
    void onSettings() {
        presenter.onSettings();
    }
}
