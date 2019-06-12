import Helper from '@ember/component/helper';
import { isEmpty } from '@ember/utils';

export default Helper.extend({

  compute(params) {
    let pictureListOne = params[0];
    let pictureListTwo = params[1];

    if(isEmpty(pictureListOne) && isEmpty(pictureListTwo)) {
      return true;
    } else {
      return false;
    }
  }
  
});
