package net.legrange.orm.mojo;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import static java.lang.String.format;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import net.legrange.orm.Database;
import net.legrange.orm.Table;
import net.legrange.orm.mojo.annotated.AnnotatedPojoGenerator;
import org.apache.maven.artifact.DependencyResolutionRequiredException;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Component;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.plugins.annotations.ResolutionScope;
import org.apache.maven.project.MavenProject;

/**
 *
 * @author gideon
 */
@Mojo(name = "generate-model", defaultPhase = LifecyclePhase.PROCESS_CLASSES,
        requiresDependencyResolution = ResolutionScope.COMPILE)
public class GenerateModel extends AbstractMojo {

    @Parameter(property = "strategy", required = true)
    private Generator.PojoStrategy strategy;
    @Parameter(property = "packages", required = true)
    private Set<String> packages;
    @Parameter(property = "outputDir", required = true)
    private String outputDir;
    @Parameter(property = "resourceDir", required = true)
    private String resourceDir;
    @Parameter(property = "database", required = true)
    private String database;
    @Component
    private MavenProject project;

    private Generator gen;
    private Modeller modeller;
    private PrintWriter svc;

    public GenerateModel() {
    }

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        try {
            switch (strategy) {
                case annotated:
                    gen = new AnnotatedPojoGenerator(this);
                    break;
                default:
                    throw new MojoExecutionException(format("Unsupported POJO strategy '%s'. BUG?", strategy));
            }
            File dir = new File(resourceDir + "/META-INF/services/");
            if (!dir.exists()) {
                dir.mkdirs();
            }
            Set<Class<?>> allPojoClasses = gen.getAllPojoClasses();
            modeller = new Modeller(gen);
            Map<String, PackageDatabase> packageDatabases = modeller.getPackageDatabases();
            svc = new PrintWriter(new FileWriter(resourceDir + "/META-INF/services/" + Database.class.getCanonicalName()));
            Set<Output> outputs = new HashSet();
            for (String pkg : packageDatabases.keySet()) {
                PackageDatabase database = packageDatabases.get(pkg);
                database.setSqlDatabase(this.database);
                Output output = new Output(this, database, pkg);
                for (Table table : database.getTables()) {
                    output.addTable(table);
                }
                outputs.add(output);
                svc.println(output.getPackageName() + ".Tables");

            }
            svc.close();
            for (Output out : outputs) {
                out.output(outputDir);
            }
        } catch (GeneratorException | OrmMetaDataException | IOException ex) {
            throw new MojoExecutionException(ex.getMessage(), ex);
        }
    }

    /**
     * Get a class loader that will load classes compiled during the build
     *
     * @return The class loader
     * @throws net.legrange.orm.mojo.GeneratorException
     * @throws org.apache.maven.artifact.DependencyResolutionRequiredException
     */
    public ClassLoader getCompiledClassesLoader() throws GeneratorException, DependencyResolutionRequiredException {
        List<String> classpathElements = project.getCompileClasspathElements();
        List<URL> projectClasspathList = new ArrayList<>();
        for (String element : classpathElements) {
            try {
                projectClasspathList.add(new File(element).toURI().toURL());
            } catch (MalformedURLException e) {
                throw new GeneratorException(element + " is an invalid classpath element", e);
            }
        }
        return new URLClassLoader(projectClasspathList.toArray(new URL[]{}), Thread.currentThread().getContextClassLoader());

    }

    public Set<String> getPackages() {
        return packages;
    }

    String getTablesPackageFor(Table table) {
        return modeller.getPackageDatabase(table.getObjectClass().getCanonicalName()).getPackageName();
    }

}
