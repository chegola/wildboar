import { element, by, promise, ElementFinder } from 'protractor';

export class StudentComponentsPage {
    createButton = element(by.id('jh-create-entity'));
    title = element.all(by.css('jhi-student div h2#page-heading span')).first();

    clickOnCreateButton(): promise.Promise<void> {
        return this.createButton.click();
    }

    getTitle(): any {
        return this.title.getAttribute('jhiTranslate');
    }
}

export class StudentUpdatePage {
    pageTitle = element(by.id('jhi-student-heading'));
    saveButton = element(by.id('save-entity'));
    cancelButton = element(by.id('cancel-save'));
    nameInput = element(by.id('field_name'));
    surnameInput = element(by.id('field_surname'));
    studentIdInput = element(by.id('field_studentId'));

    getPageTitle() {
        return this.pageTitle.getAttribute('jhiTranslate');
    }

    setNameInput(name): promise.Promise<void> {
        return this.nameInput.sendKeys(name);
    }

    getNameInput() {
        return this.nameInput.getAttribute('value');
    }

    setSurnameInput(surname): promise.Promise<void> {
        return this.surnameInput.sendKeys(surname);
    }

    getSurnameInput() {
        return this.surnameInput.getAttribute('value');
    }

    setStudentIdInput(studentId): promise.Promise<void> {
        return this.studentIdInput.sendKeys(studentId);
    }

    getStudentIdInput() {
        return this.studentIdInput.getAttribute('value');
    }

    save(): promise.Promise<void> {
        return this.saveButton.click();
    }

    cancel(): promise.Promise<void> {
        return this.cancelButton.click();
    }

    getSaveButton(): ElementFinder {
        return this.saveButton;
    }
}
