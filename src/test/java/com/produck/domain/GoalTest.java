package com.produck.domain;

import static com.produck.domain.GoalTestSamples.*;
import static com.produck.domain.LedgerTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.produck.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class GoalTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Goal.class);
        Goal goal1 = getGoalSample1();
        Goal goal2 = new Goal();
        assertThat(goal1).isNotEqualTo(goal2);

        goal2.setId(goal1.getId());
        assertThat(goal1).isEqualTo(goal2);

        goal2 = getGoalSample2();
        assertThat(goal1).isNotEqualTo(goal2);
    }

    @Test
    void ledgerTest() throws Exception {
        Goal goal = getGoalRandomSampleGenerator();
        Ledger ledgerBack = getLedgerRandomSampleGenerator();

        goal.setLedger(ledgerBack);
        assertThat(goal.getLedger()).isEqualTo(ledgerBack);

        goal.ledger(null);
        assertThat(goal.getLedger()).isNull();
    }
}
