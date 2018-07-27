import { browser } from 'protractor';
import { NavBarPage } from './../../page-objects/jhi-page-objects';
import { StudentComponentsPage, StudentUpdatePage } from './student.page-object';

describe('Student e2e test', () => {
    let navBarPage: NavBarPage;
    let studentUpdatePage: StudentUpdatePage;
    let studentComponentsPage: StudentComponentsPage;

    beforeAll(() => {
        browser.get('/');
        browser.waitForAngular();
        navBarPage = new NavBarPage();
        navBarPage.getSignInPage().autoSignInUsing('admin', 'admin');
        browser.waitForAngular();
    });

    it('should load Students', () => {
        navBarPage.goToEntity('student');
        studentComponentsPage = new StudentComponentsPage();
        expect(studentComponentsPage.getTitle()).toMatch(/wildboarApp.student.home.title/);
    });

    it('should load create Student page', () => {
        studentComponentsPage.clickOnCreateButton();
        studentUpdatePage = new StudentUpdatePage();
        expect(studentUpdatePage.getPageTitle()).toMatch(/wildboarApp.student.home.createOrEditLabel/);
        studentUpdatePage.cancel();
    });

    it('should create and save Students', () => {
        studentComponentsPage.clickOnCreateButton();
        studentUpdatePage.setNameInput('name');
        expect(studentUpdatePage.getNameInput()).toMatch('name');
        studentUpdatePage.setSurnameInput('surname');
        expect(studentUpdatePage.getSurnameInput()).toMatch('surname');
        studentUpdatePage.setStudentIdInput('studentId');
        expect(studentUpdatePage.getStudentIdInput()).toMatch('studentId');
        studentUpdatePage.save();
        expect(studentUpdatePage.getSaveButton().isPresent()).toBeFalsy();
    });

    afterAll(() => {
        navBarPage.autoSignOut();
    });
});
