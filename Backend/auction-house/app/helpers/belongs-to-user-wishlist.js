import { helper } from '@ember/component/helper';

export default Ember.Helper.extend({
  
  compute(params) {
    let wishlist = params[0];
    var productID = params[1];
    var checker = null;

    wishlist.forEach((item, index) => {
      if(item.product.get('id') == productID){
        checker = item;
        console.log(item.product.get('id'))
      }
    });

    // console.log(wishlist);
    this.notifyPropertyChange('checker');
    return checker;
  }
});
