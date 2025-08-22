import { HttpRequest, HttpHandlerFn, HttpEvent } from '@angular/common/http';
import { Observable, catchError, switchMap, throwError, BehaviorSubject, filter, take } from 'rxjs';
import { inject } from '@angular/core';
import { AuthService } from '../_services/AuthService';

const isRefreshingSubject = new BehaviorSubject<boolean>(false);
const refreshTokenSubject = new BehaviorSubject<string | null>(null);

export const authInterceptorFn = (req: HttpRequest<any>, next: HttpHandlerFn): Observable<HttpEvent<any>> => {
  const authService = inject(AuthService);

  return next(req).pipe(
    catchError(error => {
      if (error.status === 401 && !req.url.includes('/auth/refresh-token')) {
        if (isRefreshingSubject.value) {
          return refreshTokenSubject.pipe(
            filter(token => token !== null),
            take(1),
            switchMap(() => next(req))
          );
        } else {
          isRefreshingSubject.next(true);
          refreshTokenSubject.next(null);

          return authService.refreshToken().pipe(
            switchMap(() => {
              isRefreshingSubject.next(false);
              refreshTokenSubject.next('refreshed');
              return next(req);
            }),
            catchError(err => {
              isRefreshingSubject.next(false);
              authService.logout().subscribe();
              return throwError(() => err);
            })
          );
        }
      }
      return throwError(() => error);
    })
  );
};









// import { HttpInterceptorFn } from '@angular/common/http';
// import { HttpRequest, HttpHandlerFn, HttpEvent, HttpErrorResponse } from '@angular/common/http';
// import { inject } from '@angular/core';
// import { TokenStorageService } from '../_services/tokenStorageService';
// import { AuthService } from '../_services/AuthService';
// import {
//   BehaviorSubject,
//   Observable,
//   catchError,
//   filter,
//   switchMap,
//   take,
//   throwError
// } from 'rxjs';

// let isRefreshing = false;
// const refreshTokenSubject = new BehaviorSubject<string | null>(null);

// export const AuthInterceptor: HttpInterceptorFn = (
//   req: HttpRequest<any>,
//   next: HttpHandlerFn
// ): Observable<HttpEvent<any>> => {
//   const tokenStorage = inject(TokenStorageService);
//   const authService = inject(AuthService);

//   const isRefreshUrl = req.url.includes('/auth/refresh-token');
//   const accessToken = tokenStorage.getToken();

//   if (accessToken && !isRefreshUrl) {
//     req = addTokenHeader(req, accessToken);
//   }

//   return next(req).pipe(
//     catchError((error: HttpErrorResponse) => {
//       if (error.status === 401 && !isRefreshUrl) {
//         return handle401Error(req, next, tokenStorage, authService);
//       }
//       return throwError(() => error);
//     })
//   );
// };

// function addTokenHeader(request: HttpRequest<any>, token: string): HttpRequest<any> {
//   return request.clone({
//     headers: request.headers.set('Authorization', `Bearer ${token}`)
//   });
// }

// function handle401Error(
//   request: HttpRequest<any>,
//   next: HttpHandlerFn,
//   tokenStorage: TokenStorageService,
//   authService: AuthService
// ): Observable<HttpEvent<any>> {
//   if (!isRefreshing) {
//     isRefreshing = true;
//     refreshTokenSubject.next(null);

//     return authService.refreshToken().pipe(
//       switchMap((tokens: any) => {
//         tokenStorage.saveToken(tokens.accessToken);
//         tokenStorage.saveRefreshToken(tokens.refreshToken);
//         refreshTokenSubject.next(tokens.accessToken);
//         isRefreshing = false;

//         return next(addTokenHeader(request, tokens.accessToken));
//       }),
//       catchError(err => {
//         isRefreshing = false;
//         refreshTokenSubject.next(null);
//         tokenStorage.clear();
//         return throwError(() => err);
//       })
//     );
//   } else {
//     return refreshTokenSubject.pipe(
//       filter(token => token !== null),
//       take(1),
//       switchMap(token => next(addTokenHeader(request, token!)))
//     );
//   }
// }
