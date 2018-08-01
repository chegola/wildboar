import { Component, OnInit } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { LoginService } from '../../core/login/login.service';
import { CookieService } from 'ngx-cookie';
import { StateStorageService } from '../../core/auth/state-storage.service';
import { LineLogin } from '../../shared/login/line.model';

@Component({
    selector: 'jhi-auth',
    template: ''
})
export class SocialAuthComponent implements OnInit {
    constructor(
        private loginService: LoginService,
        private cookieService: CookieService,
        private router: Router,
        private route: ActivatedRoute,
        private eventManager: JhiEventManager,
        private stateStorageService: StateStorageService
    ) {
    }

    ngOnInit() {
        this.route.queryParams.subscribe(params => {
                const success = params['success'];
                if (success === 'true') {
                    this.loginService.authFromLINE().subscribe((res: LineLogin) => {
                        const token = res.token;
                        this.loginService.loginWithToken(token, false).then(() => {
                            this.cookieService.remove('social-authentication'); // just play safe
                            this.eventManager.broadcast({
                                name: 'authenticationSuccess',
                                content: 'Sending Authentication Success'
                            });
                            this.stateStorageService.storeUrl(null);
                            this.router.navigate(['settings']);
                        }, () => {
         //                   this.openSnackBar(this.translateService.instant('login.messages.error.authentication'))
                            this.router.navigate(['']);
                        });
                    }, response => {
                        this.processError(response);
                        this.router.navigate(['']);
                        });
                } else {
           //         this.openSnackBar(this.translateService.instant('login.messages.error.authentication'))
                    this.router.navigate(['']);
                }

              });
    }

    private processError(response) {
        // if (response.status === 400 && response.json().type === LOGIN_ALREADY_USED_TYPE) {
        //    this.openSnackBar(this.translateService.instant('register.messages.error.userexists'))
        // } else if (response.status === 400 && response.json().type === EMAIL_ALREADY_USED_TYPE) {
        //    this.openSnackBar(this.translateService.instant('register.messages.error.emailexists'))
        // } else {
        //    this.openSnackBar(this.translateService.instant('login.messages.error.authentication'));
        // }
    }

    authFromLine() {
        this.route.queryParams
            .filter(params => params.token)
            .subscribe(params => {
                const token = params.token;
                if (token.length) {
                    this.loginService.loginWithToken(token, false).then(() => {
                        // this.cookieService.remove('social-authentication');

                        this.eventManager.broadcast({
                            name: 'authenticationSuccess',
                            content: 'Sending Authentication Success'
                        });
                        this.stateStorageService.storeUrl(null);
                        this.router.navigate(['']);
                    }, () => {
                        this.router.navigate(['social-register'], { queryParams: { 'success': 'false' } });
                    });
                }
            });
    }
}
