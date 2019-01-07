import Helper from '@ember/component/helper';

export default Helper.extend({
  
  compute(params) {
    let wishlist = params[0];
    var productID = params[1];
    var checker = null;

    wishlist.forEach((item) => {
      if(item.product.get('id') == productID){
        checker = item;
      }
    });

    this.notifyPropertyChange('checker');
    return checker;
  }
});
