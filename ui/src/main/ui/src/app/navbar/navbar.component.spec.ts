import {async, ComponentFixture, TestBed} from '@angular/core/testing';

import {NavbarComponent, NavBarModule} from './navbar.component';

describe('NavbarComponent', () => {
  let fixture: ComponentFixture<NavbarComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [NavBarModule],
    }).compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(NavbarComponent);
    fixture.detectChanges();
  });

  // Note: Add tests as logic is added to navbar class.
});
