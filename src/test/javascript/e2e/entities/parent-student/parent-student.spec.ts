import { browser } from 'protractor';
import { NavBarPage } from './../../page-objects/jhi-page-objects';
import { ParentStudentComponentsPage, ParentStudentUpdatePage } from './parent-student.page-object';

describe('ParentStudent e2e test', () => {
    let navBarPage: NavBarPage;
    let parentStudentUpdatePage: ParentStudentUpdatePage;
    let parentStudentComponentsPage: ParentStudentComponentsPage;

    beforeAll(() => {
        browser.get('/');
        browser.waitForAngular();
        navBarPage = new NavBarPage();
        navBarPage.getSignInPage().autoSignInUsing('admin', 'admin');
        browser.waitForAngular();
    });

    it('should load ParentStudents', () => {
        navBarPage.goToEntity('parent-student');
        parentStudentComponentsPage = new ParentStudentComponentsPage();
        expect(parentStudentComponentsPage.getTitle()).toMatch(/wildboarApp.parentStudent.home.title/);
    });

    it('should load create ParentStudent page', () => {
        parentStudentComponentsPage.clickOnCreateButton();
        parentStudentUpdatePage = new ParentStudentUpdatePage();
        expect(parentStudentUpdatePage.getPageTitle()).toMatch(/wildboarApp.parentStudent.home.createOrEditLabel/);
        parentStudentUpdatePage.cancel();
    });

    /* it('should create and save ParentStudents', () => {
        parentStudentComponentsPage.clickOnCreateButton();
        parentStudentUpdatePage.userSelectLastOption();
        parentStudentUpdatePage.studentSelectLastOption();
        parentStudentUpdatePage.save();
        expect(parentStudentUpdatePage.getSaveButton().isPresent()).toBeFalsy();
    });*/

    afterAll(() => {
        navBarPage.autoSignOut();
    });
});
