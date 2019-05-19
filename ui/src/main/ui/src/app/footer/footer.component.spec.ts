import {async, ComponentFixture, TestBed} from '@angular/core/testing';

import {FooterComponent, FooterModule} from './footer.component';


describe('FooterComponent', () => {
  let fixture: ComponentFixture<FooterComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [FooterModule],
    }).compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(FooterComponent);
    fixture.detectChanges();
  });

  it('should have a link to ...', () => {

  });
});
