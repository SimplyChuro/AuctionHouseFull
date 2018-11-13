import { helper } from '@ember/component/helper';
import { isBlank } from '@ember/utils';

export function isParent(params) {
    if (params == '') {
    	return  true;
    }else{
		return false;
    }
}

export default helper(isParent);
