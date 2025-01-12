import {TestBed} from '@angular/core/testing';
import {HttpErrorResponse, HttpHandler, HttpRequest, HttpResponse, provideHttpClient} from '@angular/common/http';
import {of, throwError} from 'rxjs';
import {AuthInterceptor} from './auth.interceptor';
import {AuthService} from './auth.service';

describe('AuthInterceptor', () => {
  let interceptor: AuthInterceptor;
  let authServiceSpy: jasmine.SpyObj<AuthService>;

  beforeEach(() => {
    authServiceSpy = jasmine.createSpyObj('AuthService', ['getToken']);

    TestBed.configureTestingModule({
      providers: [
        AuthInterceptor,
        {provide: AuthService, useValue: authServiceSpy},
        provideHttpClient()
      ]
    });

    interceptor = TestBed.inject(AuthInterceptor);
  });

  it('should add an Authorization header if token is present', () => {
    const token = 'sample-token';
    authServiceSpy.getToken.and.returnValue(token);

    const req = new HttpRequest('GET', '/test');
    const next = {
      handle: (req: HttpRequest<any>) => {
        expect(req.headers.has('Authorization')).toBeTrue();
        expect(req.headers.get('Authorization')).toBe(token);
        return of(new HttpResponse());
      }
    };

    interceptor.intercept(req, next as HttpHandler).subscribe();
  });

  it('should not add an Authorization header if token is not present', () => {
    authServiceSpy.getToken.and.returnValue(null);

    const req = new HttpRequest('GET', '/test');
    const next = {
      handle: (req: HttpRequest<any>) => {
        expect(req.headers.has('Authorization')).toBeFalse();
        return of(new HttpResponse());
      }
    };

    interceptor.intercept(req, next as HttpHandler).subscribe();
  });

  it('should forward the body of the request correctly', () => {
    const token = 'sample-token';
    authServiceSpy.getToken.and.returnValue(token);

    const req = new HttpRequest('POST', '/test', {data: 'test data'});
    const next = {
      handle: (req: HttpRequest<any>) => {
        const response = new HttpResponse({body: {data: 'test data'}, status: 200});
        expect(response.body).toEqual({data: 'test data'});
        return of(response);
      }
    };

    interceptor.intercept(req, next as HttpHandler).subscribe(response => {
      if (response instanceof HttpResponse) {
        expect(response.body).toEqual({data: 'test data'});
      } else {
        throw new Error('Response is not an instance of HttpResponse');
      }
    });
  });


  it('should handle 500 error response and forward it', () => {
    const token = 'sample-token';
    authServiceSpy.getToken.and.returnValue(token);

    const req = new HttpRequest('GET', '/test');
    const next = {
      handle: (req: HttpRequest<any>) => {
        return throwError(() => new HttpErrorResponse({status: 500, statusText: 'Internal Server Error'}));
      }
    };

    interceptor.intercept(req, next as HttpHandler).subscribe({
      next: () => {
      },
      error: (error) => {
        expect(error.status).toBe(500);
        expect(error.statusText).toBe('Internal Server Error');
      }
    });
  });
});
