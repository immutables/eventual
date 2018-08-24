/*
    Copyright 2015-2018 Immutables.org authors

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
 */
package org.immutables.eventual;

import java.util.concurrent.atomic.AtomicInteger;
import javax.inject.Singleton;
import org.junit.Test;
import static org.immutables.check.Checkers.check;

@Singleton
class SampleBlackholeProviders {
  final AtomicInteger counter = new AtomicInteger();

  @Eventually.Provides
  Integer value() {
    return 10;
  }

  @Eventually.Provides
  void init() {
    counter.incrementAndGet();
  }

  @Eventually.Provides
  void init(Integer value) {
    counter.addAndGet(value);
  }
}

public class BlackholeTest {

  @Test
  public void blackhole() {
    SampleBlackholeProviders providers = new SampleBlackholeProviders();

    new EventualModules.Builder()
        .add(providers)
        .joinInjector();

    check(providers.counter.get()).is(11);
  }
}
