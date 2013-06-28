package by.hzv.patcher.audit

import by.hzv.patcher.audit.PatcherAudit;

import by.hzv.patcher.util.PatchNameParser

import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.testng.annotations.BeforeClass
import org.testng.annotations.DataProvider
import org.testng.annotations.Test

import static org.mockito.Mockito.doReturn
import static org.mockito.Mockito.mock
import static org.mockito.Mockito.spy
import static org.mockito.Mockito.when
import static org.fest.assertions.Assertions.assertThat


/**
* Basic test for PatcherAudit class
*
* @author <a href="mailto:abelogrudo@wiley.com">Alexander Belogrudov</a>
* @since 05 16, 2013
* @since 5/16/13
 */
class PatcherAuditTest {
    PatcherAudit sut = spy(new PatcherAudit())

    @Mock File mockFile1
    @Mock File mockFile2
    @Mock File mockDir

    @BeforeClass
    public void init() {
        MockitoAnnotations.initMocks(this)

        File [] files = [mockFile1, mockFile2]

        when(mockDir.listFiles()).thenReturn(files)
        when(mockDir.exists()).thenReturn(true)
        when(mockDir.isDirectory()).thenReturn(true)
        when(mockFile1.isFile()).thenReturn(true)
        when(mockFile2.isFile()).thenReturn(true)

        doReturn(1).when(sut).getLastPatchNumber()
        doReturn(mockDir).when(sut).getPatchesDir()
    }

    @Test(dataProvider = "patchFileNameProvider")
    public void shouldReturnProperlyAscOrderedFilesFromPatchesDir(String patchName1, patchName2) {
        //given
        when(mockFile1.name).thenReturn(patchName1)
        when(mockFile2.name).thenReturn(patchName2)

        def patchesNames = []

        //when
        sut.checkForPendingPatches(patchesNames)

        //then
        assertThat patchesNames hasSize 2
        assertThat (getPatchNumber(patchesNames.get(0)) < getPatchNumber(patchesNames.get(1))) isTrue()

    }

    @DataProvider
    Object[][] patchFileNameProvider() {
        [
                // Normal order (Windows OS)
                ["patch_002_SQL_00941_createTmdTable.sql", "patch_003_SQL_00941_tmdData.sql"],
                // Possible reverse order (Linux OS)
                ["patch_003_SQL_00941_tmdData.sql", "patch_002_SQL_00941_createTmdTable.sql"]
                //patch_001_SQL_00000_bootstrapGateway.sql
        ]

    }

    private int getPatchNumber(String patchName) {
        PatchNameParser.parse(patchName).number
    }
}
