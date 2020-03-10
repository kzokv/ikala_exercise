package org.ikala.helper;


import org.ikala.manager.MavenArgsManager;
import org.ikala.manager.PageObjectManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ContextDataProtoTypeHolder {
    @Autowired
    private PageObjectManager pageObjectManager;
    @Autowired
    private MavenArgsManager mavenArgsManager;


    public PageObjectManager getPageObjectManager() {
        return pageObjectManager;
    }

    public MavenArgsManager getMavenArgsManager() {
        return mavenArgsManager;
    }


}
