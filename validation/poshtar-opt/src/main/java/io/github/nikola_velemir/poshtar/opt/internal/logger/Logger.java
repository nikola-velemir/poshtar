package io.github.nikola_velemir.poshtar.opt.internal.logger;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Element;

public interface Logger {
    public void log(ProcessingEnvironment env, String errorMessage, Element element);
}
