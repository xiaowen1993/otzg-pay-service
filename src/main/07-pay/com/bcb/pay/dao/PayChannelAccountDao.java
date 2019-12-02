package com.bcb.pay.dao;

import com.bcb.base.AbstractDao;
import com.bcb.pay.entity.PayChannelAccount;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

/**
 * @Author G.
 * @Date 2019/11/25 0025 上午 11:07
 */
public interface PayChannelAccountDao  extends AbstractDao<PayChannelAccount,Long> {
    @Query("select pca from PayChannelAccount pca join pca.payAccount pa where pa.id=?1 and pca.payChannel=?2")
    Optional<PayChannelAccount> findByPayAccountIdAndPayChannel(Long payAccountId, String payChannel);

    @Query("select pca from PayChannelAccount pca join pca.payAccount pa where pa.unitId=?1 and pca.payChannel=?2")
    Optional<PayChannelAccount>  findByUnitAndPayChannel(String unitId, String payChannel);
}
