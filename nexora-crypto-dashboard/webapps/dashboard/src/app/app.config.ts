// import { ApplicationConfig, provideZoneChangeDetection } from '@angular/core';
// import { provideRouter, withHashLocation } from '@angular/router';
// import { provideHttpClient, withInterceptors } from '@angular/common/http';
// import { AuthInterceptor } from './_utils/auth-interceptor';

// import { routes } from './app.routes';

// export const appConfig: ApplicationConfig = {
//   providers: [provideZoneChangeDetection({ eventCoalescing: true }), provideRouter(routes, withHashLocation()), provideHttpClient(withInterceptors([AuthInterceptor]))]
// };

import { ApplicationConfig, InjectionToken } from '@angular/core';
import { provideHttpClient, withInterceptors } from '@angular/common/http';
import { authInterceptor } from './_utils/auth-interceptor';

export const appConfig: ApplicationConfig = {
  providers: [
    provideHttpClient(
      withInterceptors([authInterceptor])
    )
  ]
};