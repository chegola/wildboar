import { Route } from '@angular/router';
import { UserRouteAccessService } from 'app/core';
import { SocialAuthComponent } from './social-auth.component';

export const lineAuthRoute: Route = {
    path: 'line-auth',
    component: SocialAuthComponent,
    data: {
        authorities: [],
        pageTitle: 'social.register.title'
    },
    canActivate: [UserRouteAccessService]
};
