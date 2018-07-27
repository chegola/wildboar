import { element, by, promise, ElementFinder } from 'protractor';

export class ParentStudentComponentsPage {
    createButton = element(by.id('jh-create-entity'));
    title = element.all(by.css('jhi-parent-student div h2#page-heading span')).first();

    clickOnCreateButton(): promise.Promise<void> {
        return this.createButton.click();
    }

    getTitle(): any {
        return this.title.getAttribute('jhiTranslate');
    }
}

export class ParentStudentUpdatePage {
    pageTitle = element(by.id('jhi-parent-student-heading'));
    saveButton = element(by.id('save-entity'));
    cancelButton = element(by.id('cancel-save'));
    userSelect = element(by.id('field_user'));
    studentSelect = element(by.id('field_student'));

    getPageTitle() {
        return this.pageTitle.getAttribute('jhiTranslate');
    }

    userSelectLastOption(): promise.Promise<void> {
        return this.userSelect
            .all(by.tagName('option'))
            .last()
            .click();
    }

    userSelectOption(option): promise.Promise<void> {
        return this.userSelect.sendKeys(option);
    }

    getUserSelect(): ElementFinder {
        return this.userSelect;
    }

    getUserSelectedOption() {
        return this.userSelect.element(by.css('option:checked')).getText();
    }

    studentSelectLastOption(): promise.Promise<void> {
        return this.studentSelect
            .all(by.tagName('option'))
            .last()
            .click();
    }

    studentSelectOption(option): promise.Promise<void> {
        return this.studentSelect.sendKeys(option);
    }

    getStudentSelect(): ElementFinder {
        return this.studentSelect;
    }

    getStudentSelectedOption() {
        return this.studentSelect.element(by.css('option:checked')).getText();
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
