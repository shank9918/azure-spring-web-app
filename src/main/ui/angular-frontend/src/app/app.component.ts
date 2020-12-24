import {Component, OnInit} from '@angular/core';
import {BroadcastService, MsalService} from '@azure/msal-angular';
import {CryptoUtils, Logger} from 'msal';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit {
  title = 'Angular Todo App';
  isIframe = false;
  loggedIn = false;

  constructor(private broadcastService: BroadcastService, private authService: MsalService) {
  }

  // tslint:disable-next-line:typedef
  ngOnInit() {
    this.isIframe = window !== window.parent && !window.opener;

    this.checkoutAccount();

    this.broadcastService.subscribe('msal:loginSuccess', (payload) => {
      console.log(payload);
      this.checkoutAccount();
    });

    this.broadcastService.subscribe('msal:loginFailure', (payload) => {
      console.log(payload);
      console.log('login failed');
    });

    this.authService.handleRedirectCallback((authError, response) => {
      if (authError) {
        console.error('Redirect Error: ', authError.errorMessage);
        return;
      }
      console.log('Redirect Success: ', response.accessToken);
    });

    this.authService.setLogger(new Logger((logLevel, message, piiEnabled) => {
      console.log('MSAL Logging: ', message);
    }, {
      correlationId: CryptoUtils.createNewGuid(),
      piiLoggingEnabled: false
    }));
  }

  // tslint:disable-next-line:typedef
  checkoutAccount() {
    this.loggedIn = !!this.authService.getAccount();
  }

  // tslint:disable-next-line:typedef
  login() {
    const isIE = window.navigator.userAgent.indexOf('MSIE ') > -1 || window.navigator.userAgent.indexOf('Trident/') > -1;

    if (isIE) {
      this.authService.loginRedirect();
    } else {
      this.authService.loginPopup();
    }
  }

  // tslint:disable-next-line:typedef
  logout() {
    this.authService.logout();
  }
}
