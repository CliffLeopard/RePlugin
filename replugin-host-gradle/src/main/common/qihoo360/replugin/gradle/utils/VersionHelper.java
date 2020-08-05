package qihoo360.replugin.gradle.utils;

import org.gradle.api.Project;
import org.gradle.api.artifacts.Configuration;
import org.gradle.api.artifacts.Dependency;
import org.gradle.api.artifacts.DependencySet;

public class VersionHelper {

    public static class Version {
        public int major;
        public int minor;
        public int patch;

        Version() {
        }

        Version(String ver) {
            if (ver != null) {
                String[] vers = ver.split("\\.");
                if (vers.length > 0) {
                    major = Integer.parseInt(vers[0]);
                    if (vers.length > 1) {
                        minor = Integer.parseInt(vers[1]);
                        if (vers.length > 2) {
                            patch = Integer.parseInt(vers[2]);
                        }
                    }
                } else {
                    System.out.println("ver string array is empty");
                }
            }
        }

        @Override
        public String toString() {
            return "Version{" +
                    "major=" + major +
                    ", minor=" + minor +
                    ", patch=" + patch +
                    '}';
        }
    }

    /**
     * return version of com.android.tools.build:gradle
     * @param project input project
     * @return version
     */
    public static Version getAndroidBuildGradleVersion(Project project) {
        Configuration configuration = project.getBuildscript().getConfigurations().getByName("classpath");
        DependencySet dependencySet = configuration.getDependencies();
        for (Dependency dependency: dependencySet) {
            if ("com.android.tools.build".equalsIgnoreCase(dependency.getGroup())
                    && "gradle".equalsIgnoreCase(dependency.getName())) {
                return new Version(dependency.getVersion());
            }
        }
        Project rootProject = project.getRootProject();
        if (project.equals(rootProject)) {
            return new Version();
        } else {
            return getAndroidBuildGradleVersion(project.getRootProject());
        }
    }

}
