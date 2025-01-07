import {TestBed} from '@angular/core/testing';
import {HttpClientTestingModule, HttpTestingController} from '@angular/common/http/testing';
import {AuthService} from './auth.service';
import {environment} from '../../environments/environment';

describe('AuthService', () => {
  let service: AuthService;
  let httpMock: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    service = TestBed.inject(AuthService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should login successfully', async () => {
    const mockToken = 'mock-token';
    const username = 'testuser';
    const password = 'password123';

    service.login(username, password).then((result) => {
      expect(result).toBe(true);
      expect(service.isAuthenticated()).toBe(true);
      expect(service.getToken()).toBe(mockToken);
    });

    const req = httpMock.expectOne(`${environment.apiUrl}/login`);
    expect(req.request.method).toBe('POST');
    req.flush({token: mockToken});
  });

  it('should fail login when credentials are incorrect', async () => {
    const username = 'wronguser';
    const password = 'wrongpassword';

    service.login(username, password).then((result) => {
      expect(result).toBe(false);
      expect(service.isAuthenticated()).toBe(false);
    });

    const req = httpMock.expectOne(`${environment.apiUrl}/login`);
    expect(req.request.method).toBe('POST');
    req.error(new ProgressEvent('Network error'));
  });

  it('should register successfully', async () => {
    const mockToken = 'mock-token';
    const username = 'newuser';
    const password = 'newpassword123';

    service.register(username, password).then((result) => {
      expect(result).toBe(true);
      expect(service.isAuthenticated()).toBe(true);
      expect(service.getToken()).toBe(mockToken);
    });

    const req = httpMock.expectOne(`${environment.apiUrl}/register`);
    expect(req.request.method).toBe('POST');
    req.flush({token: mockToken});
  });

  it('should log out the user', () => {
    service.setToken('mock-token');
    service.isAuthenticated.set(true);

    service.logout();

    expect(service.isAuthenticated()).toBe(false);
    expect(service.getToken()).toBeNull();
    expect(localStorage.getItem('auth_token')).toBeNull();
  });

  it('should persist token in localStorage and check session', () => {
    localStorage.setItem('auth_token', 'mock-token');

    TestBed.resetTestingModule();
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });

    const newService = TestBed.inject(AuthService);

    expect(newService.isAuthenticated()).toBe(true);
    expect(newService.getToken()).toBe('mock-token');
  });
});
