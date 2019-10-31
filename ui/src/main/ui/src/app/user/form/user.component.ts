import {Component, HostListener, OnInit, ViewChild} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {UserService} from "../../service/user.service";
import {Location} from '@angular/common';
import {MatDrawer, MatTreeNestedDataSource} from "@angular/material";
import {NestedTreeControl} from '@angular/cdk/tree';
import {User} from "../user.model";


(window as any).global = window;

@Component({
  selector: 'app-home',
  templateUrl: './user.component.html',
  styleUrls: ['./user.component.scss']
})
export class UserComponent implements OnInit {

  @ViewChild(MatDrawer) drawer: MatDrawer;

  user: User = null;

  isUpdate: boolean = false;

  treeDate: TreeNode[] = [];

  treeControl = new NestedTreeControl<TreeNode>(node => node.children);
  dataSource = new MatTreeNestedDataSource<TreeNode>();

  constructor(private router: Router, private activatedRoute: ActivatedRoute,
              public location: Location, private userService: UserService) {

    this.user = <User>{
      create: null,
      enabled: null,
      password: null,
      update: null,
      username: null,
      version: null,
    };

    this.user.username = this.activatedRoute.snapshot.paramMap.get('username');
    this.isUpdate = this.user.username != ":usernmae";
  }

  @HostListener('window:resize', ['$event'])
  onResize(event) {
    let width = event.target.innerWidth;
    console.log("window:resize -> " + width);
    if (width < 1000 ) {
      this.drawer.toggle(false);
    } else {
      this.drawer.toggle(true);
    }
  }

  ngOnInit() {
    //alert(id);
    this.drawer.toggle(window.innerWidth > 1000);
    this.treeDate = <TreeNode[]>[
      {
        name: 'USER',
        children: [
          {name: this.user.username}
        ]
      },
    ];

    this.dataSource.data = this.treeDate;
    this.userService.getUser(this.user.username).subscribe((user) => {
      if (user == null) {
        return;
      }
      this.user = <User>user;
    });
  }

  // hasChild = (_: number, node: TreeNode) => !!node.children && node.children.length > 0;
  hasChild(_: number, node: TreeNode): boolean {
    return !!node.children && node.children.length > 0;
  }

  update() {
    alert("update: " + JSON.stringify(this.user));
  }

  save() {
    alert("save: " + JSON.stringify(this.user));
  }
}


interface TreeNode {
  name: string;
  children?: TreeNode[];
}
