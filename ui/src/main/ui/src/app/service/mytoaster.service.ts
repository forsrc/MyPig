import {Injectable} from '@angular/core';
import {ToastrService} from 'ngx-toastr';
import {cloneDeep} from "lodash-es";


@Injectable()
export class MyToasterService {

  constructor(public toastrService: ToastrService) {
  }

  public success(message, title) {
    const opt = cloneDeep(this.toastrService.toastrConfig);
    opt.positionClass = 'toast-bottom-right';
    this.toastrService.show(message || "success" , title, opt);
  }
}
