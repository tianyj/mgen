import doit.tools
import buildimpl
import buildutil

DOIT_CONFIG = { 
    'default_tasks': ['build']
}

def task_configure():
    
    def doCfg():
        version = buildutil.getOrSetEnvVar('MGEN_BUILD_VERSION', 'SNAPSHOT')
        buildimpl.mgen_version = version
        buildimpl.mgen_jar = "mgen-compiler/target/mgen-compiler-assembly-" + version + ".jar"
        buildimpl.mgen_cmd = "java -jar ../" + buildimpl.mgen_jar + " "
        buildimpl.pluginPaths = "../mgen-javagenerator/target,../mgen-cppgenerator/target,../mgen-javascriptgenerator/target"
        buildimpl.default_cpp_build_cfg = "RelwithDebInfo" # Because VS is epicly slow in debug
        
    return {
        'actions': [buildutil.RunOnceLazy(doCfg)],
        'doc': ': Configures the build from set environmental variables (i.e. MGEN_BUILD_VERSION)',
        'verbosity': 2
    }
    
def task_zip():
    return {
        'task_dep': ['build'],
        'calc_dep': ['get_build_sources'],
        'actions': [buildimpl.create_install_zip],
        'doc': ': Create release zip',
        'verbosity': 2
    }
    
def task_install():
    return {
        'task_dep': ['zip'],
        'calc_dep': ['get_build_sources'],
        'actions': [buildimpl.install],
        'doc': ': Installs MGen into MGEN_INSTALL_PATH',
        'verbosity': 2
    }
    
def task_upload_to_culvertsoft():
    return {
        'task_dep': ['zip'],
        'calc_dep': ['get_build_sources'],
        'actions': [buildimpl.upload_to_culvertsoft],
        'doc': ': Uploads the current build to culvertsoft.se (ssh access required)',
        'verbosity': 2
    }

def task_publish_to_sonatype():
    return {
        'task_dep': ['zip'],
        'calc_dep': ['get_build_sources'],
        'actions': [buildimpl.publish_to_sonatype],
        'doc': ': Publishes the current build to sonatype/maven (sonatype access required)',
        'verbosity': 2
    }
    
def task_test():
    return {
        'task_dep': ['build', 'generate_test_models'],
        'calc_dep': ['get_postgen_sources'],
        'actions': [
            buildimpl.tests_integration_cpp,
            buildimpl.tests_integration_java,
            buildimpl.tests_integration_js,
            buildimpl.tests_normal
        ],
        'doc': ': Run all tests',
        'verbosity': 2
    }
       
def task_build():
    return {
        'task_dep': [   
            'configure',
            'generate_version_stamp_files', 
            'build_jvm'
         ],
        'actions': [],
        'doc': ': Build MGen (default task)',
        'verbosity': 2
    }
 
def task_eclipse():
    return {
        'calc_dep': ['get_build_sources'],
        'actions': [buildimpl.eclipse],
        'clean': [lambda: buildimpl.sbt_clean('.')],
        'targets': ['mgen-compiler/target'],
        'doc': ': Create eclipse project for jvm parts',
        'verbosity': 2
    }
 
def task_build_jvm():
    return {
        'calc_dep': ['get_build_sources'],
        'task_dep': ['generate_version_stamp_files'],
        'actions': [buildimpl.build_jvm_parts],
        'clean': [lambda: buildimpl.sbt_clean('.')],
        'targets': ['mgen-api/target'],
        'doc': ': Build MGen jvm parts',
        'verbosity': 2
    }

def task_generate_test_models():
    return {
        'calc_dep': ['get_test_models'],
        'task_dep': ['build'],
        'actions': [buildimpl.tests_generate_code],
        'doc': ': Generate source code for tests',
        'verbosity': 2
    }

def task_get_version_stamp():
    return {
        'task_dep': ['configure'],
        'actions': [lambda: buildimpl.getCommitDateString() + buildimpl.mgen_version],
        'doc': ': Gets current GIT version stamp'
    }

def task_generate_version_stamp_files():
    return {
        'uptodate': [doit.tools.result_dep('get_version_stamp')],
        'actions': [buildimpl.createVersionFiles],
        'doc': ': Generate git version files for MGen',
        'verbosity': 2
    }
    
def task_get_test_models():
    return buildutil.mkCalcDepFileTask(
        patterns = ['.xml'], 
        exclDirs = ['target'],
        doc = ': Finds all mgen models used in tests'
    )

def task_get_postgen_sources():
    return buildutil.mkCalcDepFileTask(   
        patterns = ['.java', '.scala', '.sbt', '.cpp', '.h', 'CMakeLists.txt'], 
        exclDirs = ['target'],
        taskDeps = ['generate_version_stamp_files', 'generate_test_models'],
        doc = ': Finds all sources after all test models have been generated' 
    )

def task_get_build_sources():
    return buildutil.mkCalcDepFileTask(   
        patterns = ['.java', '.scala', '.sbt', '.cpp', '.h', 'CMakeLists.txt'], 
        exclDirs = ['target', 'src_generated', 'test', 'mgen-integrationtests'],
        taskDeps = ['generate_version_stamp_files'],
        doc = ': Finds all sources for building MGen' 
    )
